package com.hillel.collection.linkedList;


import com.hillel.collection.myListInterface.ICollection;
import com.hillel.collection.myListInterface.IList;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class MyLinkedList<E> implements IList<E> {

    private Node<E> head;
    private Node<E> tail;
    private int size;

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        for (Node<E> currentNode = head; currentNode != null; currentNode = currentNode.next) {
            if (currentNode.value.equals(o)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private Node<E> nextNode = head;
            private Node<E> prevNode = tail;
            private Node<E> lastReturnedNode = null;
            private int nextIndex = 0;
            private int prevIndex= size-1;


            @Override
            public boolean hasNext() {

                return nextIndex < size;

            }


            @Override
            public E next() {
                if (hasNext()) {
                    nextIndex++;
                    lastReturnedNode = nextNode;
                    nextNode = (nextNode.next == null) ? nextNode = tail : nextNode.next;
                    return lastReturnedNode.value;

                }
                throw new NoSuchElementException();
            }

            public boolean hasPrev() {

                return prevIndex > 0;

            }

            public E prev() {
                if (hasPrev()) {
                    prevIndex--;
                    lastReturnedNode = prevNode;
                    prevNode = (prevNode.prev == null) ? prevNode = head : prevNode.prev;
                    return lastReturnedNode.value;

                }
                throw new NoSuchElementException();
            }


            @Override
            public void remove() {
                if (lastReturnedNode == null)
                    throw new IllegalStateException();
                Node<E> lastNext = lastReturnedNode.next;
                unlink(lastReturnedNode);
                if (nextNode == lastReturnedNode)
                    nextNode = lastNext;
                else
                    nextIndex--;
                lastReturnedNode = null;

            }

            public void prevRemove() {
                if (lastReturnedNode == null)
                    throw new IllegalStateException();
                Node<E> lastNext = lastReturnedNode.prev;
                unlink(lastReturnedNode);
                if (prevNode == lastReturnedNode)
                    prevNode = lastNext;
                else
                    prevIndex++;
                lastReturnedNode = null;

            }

        };
    }

    @Override
    public Object[] toArray() {
        int index = 0;
        for (Node<E> node = head; node != null; node = node.next) {
            if (node.value != null) {
                index++;
            }
        }
        Object[] linkedListToArray = new Object[index];
        index = 0;
        for (Node<E> node = head; node != null; node = node.next) {
            if (node.value != null) {
                linkedListToArray[index++] = node.value;
            }
        }


        return linkedListToArray;
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return null;
    }

    @Override
    public boolean add(E element) {
        Node<E> currentNode = head;
        Node<E> newNode = new Node<>(element);

        if (currentNode == null) {
            head = newNode;
            tail = head;
        } else {
            while (currentNode.next != null) {
                currentNode = currentNode.next;
            }

            currentNode.next = newNode;
            newNode.prev = currentNode;
            tail = newNode;

        }
        incrementSize();
        updateIndex();

        return true;

    }

    @Override
    public boolean remove(Object o) {
        if (size > 0) {
            for (Node<E> currentNode = head; currentNode != null; currentNode = currentNode.next) {

                if (currentNode.value.equals(o)) {
                    unlink(currentNode);
                    decrementSize();
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean containsAll(ICollection<?> c) {
        Object[] temp = c.toArray();
        for (int i = 0; i < c.size(); i++) {
            if (!contains(temp[i])) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(ICollection<? extends E> c) {
        boolean result = false;
        E[] cArray = (E[]) c.toArray();
        for (E o : cArray) {
            result = add(o);
        }
        updateIndex();
        return result;
    }

    @Override
    public boolean addAll(int index, ICollection<? extends E> c) {
        boolean result = false;
        E[] cToArray = (E[]) c.toArray();
        for (E e : cToArray) {
            add(index, e);
            index++;
            result = true;
        }
        updateIndex();
        return result;
    }

    @Override
    public boolean removeAll(ICollection<?> c) {
        boolean result = false;
        Iterator<E> iterator = MyLinkedList.this.iterator();

        while (iterator.hasNext()) {
            Object object = iterator.next();
            if (c.contains(object)) {
                iterator.remove();
                result = true;
            }

        }
        updateIndex();
        return result;
    }

    @Override
    public boolean retainAll(ICollection<?> c) {
        boolean result = false;
        Iterator<E> iterator = MyLinkedList.this.iterator();

        while (iterator.hasNext()) {
            Object object = iterator.next();
            if (!c.contains(object)) {
                iterator.remove();
                result = true;
            }

        }
        updateIndex();
        return result;
    }

    @Override
    public void clear() {
        size = 0;
        head = null;
    }

    @Override
    public E get(int index) {
        checkIndex(index);
        for (Node<E> currentNode = head; currentNode != null; currentNode = currentNode.next) {
            if (currentNode.index == index) {
                return currentNode.value;
            }
        }
        return null;
    }

    @Override
    public E set(int index, E element) {
        E result = null;
        checkIndex(index);
        for (Node<E> currentNode = head; currentNode != null; currentNode = currentNode.next) {
            if (currentNode.index == index) {
                result = currentNode.value;
                currentNode.value = element;
            }
        }
        updateIndex();
        return result;
    }


    @Override
    public void add(int index, E element) {
        checkIndex(index);

        Node<E> newNode = new Node<>(element);
        Node<E> nextNode;
        for (Node<E> currentNode = head; currentNode != null; currentNode = currentNode.next)
            if (currentNode.index == (index - 1)) {

                nextNode = currentNode.next;
                nextNode.prev = newNode;
                nextNode.index = index + 1;

                newNode.next = nextNode;
                newNode.prev = currentNode;
                newNode.index = index;

                currentNode.next = newNode;
                incrementSize();
                updateIndex();
                break;
            }


    }

    @Override
    public E remove(int index) {
        E result = null;
        checkIndex(index);
        for (Node<E> node = head; node != null; node = node.next) {
            if (node.index == index) {
                result = node.value;
                unlink(node);
                decrementSize();
                updateIndex();
            }
        }
        return result;
    }

    @Override
    public int indexOf(Object o) {
        for (Node<E> node = head; node != null; node = node.next) {
            if (node.value.equals(o)) {
                return node.index;
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        for (Node<E> node = tail; node != null; node = node.prev) {
            if (node.value.equals(o)) {
                return node.index;
            }
        }
        return -1;
    }

    @Override
    public ListIterator<E> listIterator() {
        return null;
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return null;
    }

    @Override
    public IList<E> subList(int fromIndex, int toIndex) {
        return null;
    }

    private void incrementSize() {
        size++;
    }

    private void decrementSize() {
        size--;
    }

    private E unlink(Node<E> nodeToUnlink) {

        E value = nodeToUnlink.value;
        Node<E> prev = nodeToUnlink.prev;
        Node<E> next = nodeToUnlink.next;

        if (prev == null) {
            head = next;
        } else {
            prev.next = next;
            nodeToUnlink.prev = null;
        }
        if (next == null) {
            tail = prev;
        } else {
            next.prev = prev;
            nodeToUnlink.next = null;
        }

        nodeToUnlink.value = null;
        return value;
    }

    private boolean checkIndex(int index) {
        if (index > size || index < 0) throw new IllegalStateException();
        return true;
    }

    private void updateIndex() {
        int index = 0;
        for (Node<E> currentNode = head; currentNode != null; currentNode = currentNode.next) {
            currentNode.index = index++;
        }
    }

    private static class Node<E> {
        E value;
        Node<E> next;
        Node<E> prev;
        int index = -1;

        private Node(E value) {
            this.next = null;
            this.prev = null;
            this.value = value;

        }


        private Node(Node<E> next, Node<E> prev, E value) {
            this.next = next;
            this.prev = prev;
            this.value = value;

        }

        private void setIndex(int index) {
            this.index = index;
        }
    }
}
