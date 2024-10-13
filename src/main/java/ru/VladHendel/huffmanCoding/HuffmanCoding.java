package ru.VladHendel.huffmanCoding;

import java.util.*;

//Класс для реализации алгоритма Хаффмана.
public class HuffmanCoding {
    // Map для хранения частоты каждого символа
    Map<Character, Integer> frequency = new HashMap<>();
    // Map для хранения кодов Хаффмана для каждого символа
    private Map<Character, String> huffmanCode = new HashMap<>();
    // Корень дерева Хаффмана
    HuffmanNode root;
    //Метод для подсчета частоты каждого символа в тексте.
    private void findFrequency(String text) {
        // Проходим по каждому символу в тексте
        for (char c : text.toCharArray()) {
            // Если символ еще не встречался, инициализируем его частоту нулем
            if (!frequency.containsKey(c)) {
                frequency.put(c, 0);
            }
            // Увеличиваем частоту символа на 1
            frequency.put(c, frequency.get(c) + 1);
        }
    }
    //Метод для построения дерева Хаффмана и генерации кодов для каждого символа.
    private void findRoot(String text){
        // Подсчитываем частоты символов в тексте
        findFrequency(text);
        // Создаем очередь с приоритетом для узлов дерева Хаффмана
        PriorityQueue<HuffmanNode> queue = new PriorityQueue<>();
        // Создаем листовые узлы для каждого символа и добавляем их в очередь
        for(Map.Entry<Character, Integer> entry: frequency.entrySet()){
            queue.add(new HuffmanNode(entry.getKey(), entry.getValue()));
        }
        // Цикл для построение дерева Хаффмана
        while (queue.size()>1){
            // Извлекаем два узла с наименьшей частотой
            HuffmanNode left = queue.poll();
            HuffmanNode right = queue.poll();
            // Создаем новый внутренний узел с суммарной частотой
            HuffmanNode parent = new HuffmanNode(left, right);
            // Добавляем новый узел обратно в очередь
            queue.add(parent);
        }
        // Оставшийся узел является корнем дерева
        root = queue.poll();
        // Создаем таблицу кодов Хаффмана
        createTable(root, "");
        // Выводим таблицу кодов
        System.out.println("Таблица кодов Хаффмана:");
        for (Map.Entry<Character, String> entry : huffmanCode.entrySet()) {
            System.out.println( "'" + entry.getKey() + "' : " + entry.getValue());
        }
    }

    //Рекурсивный метод для создания кодов Хаффмана для каждого символа.
    private void createTable(HuffmanNode node, String code){
        if (node != null) {
            // Если узел является листовым (символом)
            if (node.left == null && node.right == null) {
                // Добавляем символ и его код в таблицу кодов
                huffmanCode.put(node.symbol, code);
            }
            // Рекурсивный вызов для левого потомка с добавлением "0" к коду
            createTable(node.left, code + "0");
            // Рекурсивный вызов для правого потомка с добавлением "1" к коду
            createTable(node.right, code + "1");
        }

    }
    //Метод для кодирования текста с помощью кодов Хаффмана.
    public String encode(String text){
        // Строка для накопления кодированного текста
        StringBuilder encodedText = new StringBuilder();
        // Проходим по каждому символу в тексте
        for (char c : text.toCharArray()){
            // Добавляем соответствующий код символа к результату
            encodedText.append(huffmanCode.get(c));
        }
        // Выводим закодированный текст
        System.out.println("Закодированный текст:\n" + encodedText.toString());
        return encodedText.toString();
    }

    // Метод для декодирования закодированного текста.
    public String decode(String encodedText){
        // Строка для накопления декодированного текста
        StringBuilder decodedText = new StringBuilder();
        // Фиксируем текущий узел как корневой
        HuffmanNode current = root;
        // Проходим по каждому биту в закодированном тексте
        for (char bit : encodedText.toCharArray()){
            // Переходим по дереву влево или вправо в зависимости от бита
            if (bit=='0'){
                current = current.left; //влево если '0'
            } else if (bit == '1'){
                current = current.right; //вправо если '1'
            }
            // Если достигли листового узла (символа)
            if (current.left == null && current.right == null) {
                // Добавляем символ к результату
                decodedText.append(current.symbol);
                // Возвращаемся к корню для следующего символа
                current = root;
            }
            }
        return decodedText.toString();
        }



    //метод для запуска программы.
    public static void main(String[] args) {
        // Исходный текст для кодирования
        String text = "Он подошел к Анне Павловне, поцеловал ее руку, подставив ей свою надушенную и сияющую лысину, и покойно уселся на диване.\n" +
                "— Avant tout dites-moi, comment vous allez, chère amie?  Успокойте меня, — сказал он, не изменяя голоса и тоном, в котором из-за приличия и участия просвечивало равнодушие и даже насмешка.";
        // Записываем время начала выполнения
        long startTime = System.currentTimeMillis();
        // Создаем экземпляр класса HuffmanCoding
        HuffmanCoding coding = new HuffmanCoding();
        // Строим дерево Хаффмана и генерируем коды
        coding.findRoot(text);
        // Кодируем текст
        String encoded = coding.encode(text);
        // Декодируем текст
        String decoded = coding.decode(encoded);
        // Вывод декодированного текста
        System.out.println("\nДекодированный текст:\n" + decoded);
        // Проверка корректности декодирования
        if (text.equals(decoded)) {
            System.out.println("\nДекодирование успешно. Исходный и декодированный тексты совпадают.");
        } else {
            System.out.println("\nДекодирование не удалось. Тексты не совпадают.");
        }
        // Записываем время окончания выполнения
        long endTime = System.currentTimeMillis();
        // Вычисляем и выводим время выполнения
        long executionTime = endTime - startTime;
        System.out.println("Время выполнения: " + executionTime + " миллисекунд.");
    }

}

