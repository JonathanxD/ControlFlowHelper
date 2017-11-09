/**
 *
 *  Copyright 2012 Tobias Gierke <tobias.gierke@code-sourcery.de>
 *
 *  Original project:
 *
 *  2017 Rewrite in Kotlin by JonathanxD <https://github.com/JonathanxD>
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.github.jonathanxd.controlflowhelper.util;

import com.github.jonathanxd.controlflowhelper.Block;
import com.github.jonathanxd.controlflowhelper.Block;
import com.github.jonathanxd.controlflowhelper.Block;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class BlockList extends LinkedList<Block> {
    
    private final LinkedList<Block> wrapped;

    public BlockList() {
        this(new LinkedList<>());
    }

    BlockList(LinkedList<Block> wrapped) {
        this.wrapped = wrapped;
    }

    private Block last;

    public Block getBlock(int pos) {
        for (Block Block : this.wrapped) {
            if(Block.getEntryPoint() == pos)
                return Block;
        }

        Block Block = new Block(pos);

        this.addLast(Block);

        return Block;
    }
    
    @Override
    public Block getFirst() {
        return this.wrapped.getFirst();
    }

    @Override
    public Block getLast() {
        return this.wrapped.getLast();
    }

    @Override
    public Block removeFirst() {
        return this.wrapped.removeFirst();
    }

    @Override
    public Block removeLast() {
        return this.wrapped.removeLast();
    }

    @Override
    public void addFirst(Block e) {
        this.wrapped.addFirst(e);
    }

    @Override
    public void addLast(Block e) {
        this.wrapped.addLast(e);
    }

    @Override
    public boolean contains(Object o) {
        return this.wrapped.contains(o);
    }

    @Override
    public int size() {
        return this.wrapped.size();
    }

    @Override
    public boolean add(Block e) {
        return this.wrapped.add(e);
    }

    @Override
    public boolean remove(Object o) {
        return this.wrapped.remove(o);
    }

    @Override
    public boolean addAll(Collection<? extends Block> c) {
        return this.wrapped.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends Block> c) {
        return this.wrapped.addAll(index, c);
    }

    @Override
    public void clear() {
        this.wrapped.clear();
    }

    @Override
    public Block get(int index) {
        return this.wrapped.get(index);
    }

    @Override
    public Block set(int index, Block element) {
        return this.wrapped.set(index, element);
    }

    @Override
    public void add(int index, Block element) {
        this.wrapped.add(index, element);
    }

    @Override
    public Block remove(int index) {
        return this.wrapped.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return this.wrapped.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return this.wrapped.lastIndexOf(o);
    }

    @Override
    public Block peek() {
        return this.wrapped.peek();
    }

    @Override
    public Block element() {
        return this.wrapped.element();
    }

    @Override
    public Block poll() {
        return this.wrapped.poll();
    }

    @Override
    public Block remove() {
        return this.wrapped.remove();
    }

    @Override
    public boolean offer(Block e) {
        return this.wrapped.offer(e);
    }

    @Override
    public boolean offerFirst(Block e) {
        return this.wrapped.offerFirst(e);
    }

    @Override
    public boolean offerLast(Block e) {
        return this.wrapped.offerLast(e);
    }

    @Override
    public Block peekFirst() {
        return this.wrapped.peekFirst();
    }

    @Override
    public Block peekLast() {
        return this.wrapped.peekLast();
    }

    @Override
    public Block pollFirst() {
        return this.wrapped.pollFirst();
    }

    @Override
    public Block pollLast() {
        return this.wrapped.pollLast();
    }

    @Override
    public void push(Block e) {
        this.wrapped.push(e);
    }

    @Override
    public Block pop() {
        return this.wrapped.pop();
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        return this.wrapped.removeFirstOccurrence(o);
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        return this.wrapped.removeLastOccurrence(o);
    }

    @Override
    public ListIterator<Block> listIterator(int index) {
        return this.wrapped.listIterator(index);
    }

    @Override
    public Iterator<Block> descendingIterator() {
        return this.wrapped.descendingIterator();
    }

    @Override
    public Object clone() {
        return this.wrapped.clone();
    }

    @Override
    public Object[] toArray() {
        return this.wrapped.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return this.wrapped.toArray(a);
    }

    @Override
    public Spliterator<Block> spliterator() {
        return this.wrapped.spliterator();
    }

    @Override
    public Iterator<Block> iterator() {
        return this.wrapped.iterator();
    }

    @Override
    public ListIterator<Block> listIterator() {
        return this.wrapped.listIterator();
    }

    @Override
    public List<Block> subList(int fromIndex, int toIndex) {
        return this.wrapped.subList(fromIndex, toIndex);
    }

    @Override
    public boolean equals(Object o) {
        return this.wrapped.equals(o);
    }

    @Override
    public int hashCode() {
        return this.wrapped.hashCode();
    }

    @Override
    protected void removeRange(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isEmpty() {
        return this.wrapped.isEmpty();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return this.wrapped.containsAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return this.wrapped.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return this.wrapped.retainAll(c);
    }

    @Override
    public String toString() {
        return this.wrapped.toString();
    }

    @Override
    public void replaceAll(UnaryOperator<Block> operator) {
        this.wrapped.replaceAll(operator);
    }

    @Override
    public void sort(Comparator<? super Block> c) {
        this.wrapped.sort(c);
    }

    @Override
    public boolean removeIf(Predicate<? super Block> filter) {
        return this.wrapped.removeIf(filter);
    }

    @Override
    public Stream<Block> stream() {
        return this.wrapped.stream();
    }

    @Override
    public Stream<Block> parallelStream() {
        return this.wrapped.parallelStream();
    }

    @Override
    public void forEach(Consumer<? super Block> action) {
        this.wrapped.forEach(action);
    }
}
