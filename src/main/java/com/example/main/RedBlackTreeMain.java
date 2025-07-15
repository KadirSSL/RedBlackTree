package com.example.main;

import java.util.*;

class RedBlackTree {
    private static final boolean RED = true;
    private static final boolean BLACK = false;

    class Node {
        int data;
        boolean color;
        Node left, right, parent;

        Node(int data) {
            this.data = data;
            this.color = RED;
        }
    }

    private Node root;

    public void insert(int data) {
        Node node = new Node(data);
        root = bstInsert(root, node);
        fixViolation(node);
    }

    private Node bstInsert(Node root, Node node) {
        if (root == null)
            return node;

        if (node.data < root.data) {
            root.left = bstInsert(root.left, node);
            root.left.parent = root;
        } else if (node.data > root.data) {
            root.right = bstInsert(root.right, node);
            root.right.parent = root;
        }
        return root;
    }

    private void fixViolation(Node node) {
        Node parent, grandparent;

        while (node != root && node.parent != null && node.parent.color == RED) {
            parent = node.parent;
            grandparent = parent.parent;

            if (grandparent == null)
                break;

            if (parent == grandparent.left) {
                Node uncle = grandparent.right;

                if (uncle != null && uncle.color == RED) {
                    grandparent.color = RED;
                    parent.color = BLACK;
                    uncle.color = BLACK;
                    node = grandparent;
                } else {
                    if (node == parent.right) {
                        leftRotate(parent);
                        node = parent;
                        parent = node.parent;
                    }
                    rightRotate(grandparent);
                    swapColors(parent, grandparent);
                    node = parent;
                }
            } else {
                Node uncle = grandparent.left;

                if (uncle != null && uncle.color == RED) {
                    grandparent.color = RED;
                    parent.color = BLACK;
                    uncle.color = BLACK;
                    node = grandparent;
                } else {
                    if (node == parent.left) {
                        rightRotate(parent);
                        node = parent;
                        parent = node.parent;
                    }
                    leftRotate(grandparent);
                    swapColors(parent, grandparent);
                    node = parent;
                }
            }
        }
        root.color = BLACK;
    }

    private void swapColors(Node n1, Node n2) {
        boolean temp = n1.color;
        n1.color = n2.color;
        n2.color = temp;
    }

    private void leftRotate(Node node) {
        Node rightChild = node.right;
        node.right = rightChild.left;
        if (rightChild.left != null)
            rightChild.left.parent = node;

        rightChild.parent = node.parent;

        if (node.parent == null)
            root = rightChild;
        else if (node == node.parent.left)
            node.parent.left = rightChild;
        else
            node.parent.right = rightChild;

        rightChild.left = node;
        node.parent = rightChild;
    }

    private void rightRotate(Node node) {
        Node leftChild = node.left;
        node.left = leftChild.right;
        if (leftChild.right != null)
            leftChild.right.parent = node;

        leftChild.parent = node.parent;

        if (node.parent == null)
            root = leftChild;
        else if (node == node.parent.left)
            node.parent.left = leftChild;
        else
            node.parent.right = leftChild;

        leftChild.right = node;
        node.parent = leftChild;
    }

    public void delete(int data) {
        root = deleteNode(root, data);
    }

    private Node deleteNode(Node root, int key) {
        if (root == null) return root;

        if (key < root.data)
            root.left = deleteNode(root.left, key);
        else if (key > root.data)
            root.right = deleteNode(root.right, key);
        else {
            if (root.left == null)
                return root.right;
            else if (root.right == null)
                return root.left;

            Node temp = minValueNode(root.right);
            root.data = temp.data;
            root.right = deleteNode(root.right, temp.data);
        }
        return root;
    }

    private Node minValueNode(Node node) {
        Node current = node;
        while (current.left != null)
            current = current.left;
        return current;
    }

    // Traversal Methods with color info
    public void preorderTraversal() {
        preorderTraversal(root);
        System.out.println();
    }

    private void preorderTraversal(Node node) {
        if (node != null) {
            System.out.print(node.data + "(" + (node.color == RED ? "R" : "B") + ") ");
            preorderTraversal(node.left);
            preorderTraversal(node.right);
        }
    }

    public void postorderTraversal() {
        postorderTraversal(root);
        System.out.println();
    }

    private void postorderTraversal(Node node) {
        if (node != null) {
            postorderTraversal(node.left);
            postorderTraversal(node.right);
            System.out.print(node.data + "(" + (node.color == RED ? "R" : "B") + ") ");
        }
    }

    public void levelOrderTraversal() {
        if (root == null) return;

        Queue<Node> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            Node current = queue.poll();
            System.out.print(current.data + "(" + (current.color == RED ? "R" : "B") + ") ");
            if (current.left != null) queue.add(current.left);
            if (current.right != null) queue.add(current.right);
        }
        System.out.println();
    }

    public void inorderTraversal() {
        inorderTraversal(root);
        System.out.println();
    }

    private void inorderTraversal(Node node) {
        if (node != null) {
            inorderTraversal(node.left);
            System.out.print(node.data + "(" + (node.color == RED ? "R" : "B") + ") ");
            inorderTraversal(node.right);
        }
    }

    // Method to print the tree structure
    public void printTree() {
        printTree(root, "", true);
        System.out.println();
    }

    private void printTree(Node node, String prefix, boolean isLeft) {
        if (node == null) return;

        System.out.println(prefix + (isLeft ? "├── " : "└── ") + node.data + "(" + (node.color == RED ? "R" : "B") + ")");
        printTree(node.left, prefix + (isLeft ? "│   " : "    "), true);
        printTree(node.right, prefix + (isLeft ? "│   " : "    "), false);
    }
}

public class RedBlackTreeMain {
    public static void main(String[] args) {
        RedBlackTree tree = new RedBlackTree();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n1. Ekleme\n2. Silme\n3. Preorder\n4. Postorder\n5. Level Order\n6. Inorder\n7. Ağaç Yapısını Göster\n8. Çıkış");
            System.out.print("Seçiminiz: ");
            int secim = scanner.nextInt();

            switch (secim) {
                case 1:
                    System.out.print("Eklenecek sayıyı girin: ");
                    int sayiEkle = scanner.nextInt();
                    tree.insert(sayiEkle);
                    break;
                case 2:
                    System.out.print("Silinecek sayıyı girin: ");
                    int sayiSil = scanner.nextInt();
                    tree.delete(sayiSil);
                    break;
                case 3:
                    System.out.println("Preorder:");
                    tree.preorderTraversal();
                    break;
                case 4:
                    System.out.println("Postorder:");
                    tree.postorderTraversal();
                    break;
                case 5:
                    System.out.println("Level Order:");
                    tree.levelOrderTraversal();
                    break;
                case 6:
                    System.out.println("Inorder:");
                    tree.inorderTraversal();
                    break;
                case 7:
                    System.out.println("Ağaç Yapısı:");
                    tree.printTree();
                    break;
                case 8:
                    System.out.println("Çıkılıyor...");
                    return;
                default:
                    System.out.println("Geçersiz seçim!");
            }
        }
    }
}
