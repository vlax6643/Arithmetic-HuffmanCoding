package ru.VladHendel.huffmanCoding;

//Класс для представления узлов дерева Хаффмана.
public class HuffmanNode implements Comparable<HuffmanNode>{
    char symbol; // Символ, если узел листовой
    int frequency; // Частота символа или сумма частот дочерних узлов
    HuffmanNode left; // Левый потомок
    HuffmanNode right; // Правый потомок

    //Конструктор для создания листового узла (символа).
    public HuffmanNode(char symbol, int frequency){
        this.symbol = symbol;
        this.frequency = frequency;

    }
    //Конструктор для создания внутреннего узла (объединение двух узлов)
    public HuffmanNode(HuffmanNode left, HuffmanNode right){
        this.symbol = '\0'; // Символ не используется для внутренних узлов
        this.frequency = left.frequency+right.frequency; // Суммарная частота
        this.left = left;
        this.right = right;

    }

    //Метод для сравнения узлов по частоте (для очереди с приоритетом).
    @Override
    public int compareTo(HuffmanNode o) {
        return Integer.compare(this.frequency, o.frequency);
    }
}
