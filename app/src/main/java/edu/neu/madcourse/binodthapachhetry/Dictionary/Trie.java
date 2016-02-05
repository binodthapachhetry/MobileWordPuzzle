package edu.neu.madcourse.binodthapachhetry.Dictionary;


public class Trie {

    public static class Node {
        char info;
        boolean isWord;
        Node[] children;

        Node(char ch) {
            info = ch;
            isWord = false;
            children = new Node[128];
        }
    }

    private Node myRoot;

    public Trie() {
        myRoot = new Node('x');
    }

    public void add(String s) {

        Node t = myRoot;
        for (int k = 0; k < s.length(); k++) {
            char ch = Character.toLowerCase(s.charAt(k));
            if (t.children[ch] == null) {
                t.children[ch] = new Node(ch);
            }
            t = t.children[ch];
        }
        t.isWord = true;
    }

    public boolean contains(String s) {
        Node t = myRoot;
        for (int k = 0; k < s.length(); k++) {
            char ch = Character.toLowerCase(s.charAt(k));
            t = t.children[ch];
            if (t == null)
                return false;
        }
        return t.isWord;
    }

}
