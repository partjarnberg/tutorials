package buzz.programmers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

class MyLinkedListImplTest {

    private MyLinkedList<String> myLinkedList;

    @BeforeEach
    void setUp() {
        myLinkedList = new MyLinkedListImpl<>();
    }

    @Test
    void add() {
        myLinkedList.add("a");
        myLinkedList.add("b");
        myLinkedList.add("c");
        myLinkedList.add("d");

        assertThat(myLinkedList.get(0)).isEqualTo("a");
        assertThat(myLinkedList.get(1)).isEqualTo("b");
        assertThat(myLinkedList.get(2)).isEqualTo("c");
        assertThat(myLinkedList.get(3)).isEqualTo("d");
        assertThat(myLinkedList.size()).isEqualTo(4);
    }

    @Test
    void addFirst() {
        myLinkedList.addFirst("a");
        assertThat(myLinkedList.size()).isEqualTo(1);
        assertThat(myLinkedList.get(0)).isEqualTo("a");

        myLinkedList.addFirst("b");
        assertThat(myLinkedList.size()).isEqualTo(2);
        assertThat(myLinkedList.get(0)).isEqualTo("b");
        assertThat(myLinkedList.get(1)).isEqualTo("a");
    }

    @Test
    void addLast() {
        myLinkedList.addLast("a");
        assertThat(myLinkedList.size()).isEqualTo(1);
        assertThat(myLinkedList.get(0)).isEqualTo("a");

        myLinkedList.addLast("b");
        assertThat(myLinkedList.size()).isEqualTo(2);
        assertThat(myLinkedList.get(0)).isEqualTo("a");
        assertThat(myLinkedList.get(1)).isEqualTo("b");
    }

    @Test
    void addIndex() {
        myLinkedList.add(0, "a");
        myLinkedList.add(0, "b");
        myLinkedList.add(0, "c");
        myLinkedList.add(0, "d");
        assertThat(myLinkedList.size()).isEqualTo(4);
        assertThat(myLinkedList.get(3)).isEqualTo("a");
        assertThat(myLinkedList.get(2)).isEqualTo("b");
        assertThat(myLinkedList.get(1)).isEqualTo("c");
        assertThat(myLinkedList.get(0)).isEqualTo("d");
    }

    @Test
    void getFirst() {
        myLinkedList.add("a");
        myLinkedList.add("b");
        myLinkedList.add("c");
        myLinkedList.add("d");
        assertThat(myLinkedList.getFirst()).isEqualTo("a");
    }

    @Test
    void getLast() {
        myLinkedList.add("a");
        myLinkedList.add("b");
        myLinkedList.add("c");
        myLinkedList.add("d");
        assertThat(myLinkedList.getLast()).isEqualTo("d");
    }

    @Test
    void contains() {
        myLinkedList.add("Car");
        myLinkedList.add("Ball");
        myLinkedList.add("Basket");
        myLinkedList.add("Football");
        myLinkedList.add("Doll");
        myLinkedList.add("Teddybear");
        myLinkedList.add("Pen");
        myLinkedList.add("Pencil case");
        myLinkedList.add("Marbles");

        assertThat(myLinkedList.contains("Car")).isTrue();
        assertThat(myLinkedList.contains("Teddybear")).isTrue();
        assertThat(myLinkedList.contains("Marbles")).isTrue();
        assertThat(myLinkedList.contains("Something else")).isFalse();
    }

    @Test
    void iterator() {
        final String[] expected = {"Car", "Ball", "Basket", "Football", "Doll",
                "Teddybear", "Pen", "Pencil case", "Marbles"};

        myLinkedList.add("Car");
        myLinkedList.add("Ball");
        myLinkedList.add("Basket");
        myLinkedList.add("Football");
        myLinkedList.add("Doll");
        myLinkedList.add("Teddybear");
        myLinkedList.add("Pen");
        myLinkedList.add("Pencil case");
        myLinkedList.add("Marbles");

        final AtomicInteger counter = new AtomicInteger();
        for(String s : myLinkedList) {
            assertThat(s).isEqualTo(expected[counter.getAndIncrement()]);
        }

        counter.set(0);
        myLinkedList.forEach(s -> assertThat(s).isEqualTo(expected[counter.getAndIncrement()]));
    }
}