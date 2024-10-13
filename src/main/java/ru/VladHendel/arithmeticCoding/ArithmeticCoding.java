package ru.VladHendel.arithmeticCoding;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

 //Класс для реализации арифметического кодирования.
public class ArithmeticCoding {
     // Map для хранения вероятностей символов
    private LinkedHashMap<Character, Fraction> probabilities;
     // Список интервалов для каждого символа
    private ArrayList<SymbolRange> ranges;
     // Счетчик общего количества символов
    private int counter;
     // Символ остановки
    private char stopSymbol = '%';

     //Конструктор, инициализирующий вероятности и интервалы символов.
    public ArithmeticCoding(String text) {
        calculateProbabilities(text); // Расчет вероятностей символов
        calculateRanges(); // Расчет интервалов на основе вероятностей
    }


     // Расчет вероятностей каждого символа в тексте.
    private void calculateProbabilities(String text) {
        // map для хранения частот символов
        LinkedHashMap<Character, Integer> frequency = new LinkedHashMap<>();
        // Проходим по каждому символу в тексте
        for (char c : text.toCharArray()) {
            // Если символ еще не встречался, инициализируем его частоту нулем
            if (!frequency.containsKey(c)) {
                frequency.put(c, 0);
            }
            // Увеличиваем частоту символа на 1
            frequency.put(c, frequency.get(c) + 1);
            // Увеличиваем общий счетчик символов
            counter++;
        }

        // Добавляем символ остановки, если он еще не добавлен
        if (!frequency.containsKey(stopSymbol)) {
            frequency.put(stopSymbol, 1);
            // Увеличиваем общий счетчик символов
            counter++;
        }
        // Инициализируем MAPу вероятностей
        probabilities = new LinkedHashMap<>();
        // Вычисляем вероятность для каждого символа
        for (Map.Entry<Character, Integer> entry : frequency.entrySet()) {
            // Вероятность символа = частота / общее количество символов
            probabilities.put(entry.getKey(), new Fraction(entry.getValue(), counter));
        }
        // Выводим рассчитанные вероятности
        System.out.println(probabilities);
    }

    // Расчет интервалов для символов
    private void calculateRanges() {
        // Инициализируем список интервалов
        ranges = new ArrayList<>();
        Fraction lowerBound = new Fraction(0, 1); // Начальная нижняя граница [0, 1]
        // Проходим по каждому символу и его вероятности
        for (Map.Entry<Character, Fraction> entry : probabilities.entrySet()) {
            char symbol = entry.getKey();//загружаем ключ (сисвол) из мапы в переменную
            Fraction probability = entry.getValue(); // Вероятность текущего символа
            // Верхняя граница интервала = нижняя граница + вероятность символа
            Fraction upperBound = lowerBound.add(probability); // Верхняя граница интервала
            // Добавляем новый интервал для символа в список
            ranges.add(new SymbolRange(symbol, lowerBound, upperBound)); // Добавляем новый интервал в список
            // Выводим интервал символа
            System.out.println(symbol + " : [ " + lowerBound + ", " + upperBound + " ]");
            lowerBound = upperBound; // Обновление нижней границы для следующего символа
        }
    }

    // Кодирование текста
    public Fraction encode(String text) {
        // Добавляем символ остановки в конец текста
        text += stopSymbol;
        // Инициализируем начальные нижнюю и верхнюю границы интервала
        Fraction lower = new Fraction(0, 1); //нижняя граница
        Fraction upper = new Fraction(1, 1); //верхняя граница
        // Проходим по каждому символу в тексте
        for (char c : text.toCharArray()) {
            // Получаем интервал для текущего символа
            SymbolRange range = getSymbolRange(c);
            // Вычисляем ширину текущего интервала
            Fraction rangeWidth = upper.subtract(lower);
            // Обновляем верхнюю границу: нижняя_граница + ширина_интевала * верхняя_границв_символа
            upper = lower.add(rangeWidth.multiply(range.upperBound));
            // Обновляем нижнюю границу: нижняя_граница + ширина_интевала * нижняя_граница_символа
            lower = lower.add(rangeWidth.multiply(range.lowerBound));
        }
        // Возвращаем полученную дробь, представляющую закодированный текст
        return lower;
    }

    // Получение интервала для символа
    private SymbolRange getSymbolRange(char c) {
        // Проходим по списку интервалов
        for (SymbolRange range : ranges) {
            // Если символ совпадает, возвращаем интервал
            if (range.symbol == c) {
                return range;
            }
        }
        // Если символ не найден возвращаем 0
        return null;
    }

    // Декодирование закодированного сообщения
    public String decode(Fraction code) {
        // Строка для накопления декодированного текста
        StringBuilder decoded = new StringBuilder();
        // Инициализируем начальные нижнюю и верхнюю границы интервала
        Fraction lower = new Fraction(0, 1);
        Fraction upper = new Fraction(1, 1);
        //цикл декодирования символов, который будет прерываться при обнаружении символа остановки.
        while (true) {
            // Вычисляем ширину текущего интервала
            Fraction rangeWidth = upper.subtract(lower);
            // вычисляем относительное положение закодированного значения внутри текущего интервала: (code - lower) / rangeWidth
            Fraction value = code.subtract(lower).divide(rangeWidth);
            //булевая переменная, инициализируется как false. Она станет true, когда соответствующий символ будет найден.
            boolean symbolFound = false;
            // Проходим по всем интервалам символов
            for (SymbolRange symbolRange : ranges) {
                // Проверяем, попадает ли символ в интервал
                if (symbolRange.isInRange(value)) {
                    // Добавляем символ к декодированному тексту
                    decoded.append(symbolRange.symbol);
                    // Если найден символ остановки, завершаем декодирование
                    if (symbolRange.symbol == stopSymbol) {
                        // Возвращаем декодированный текст без символа остановки
                        return decoded.substring(0, decoded.length() - 1);
                    }
                    // Обновляем границы для следующего символа
                    // upper = lower + rangeWidth * upperBound символа
                    upper = lower.add(rangeWidth.multiply(symbolRange.upperBound));
                    // lower = lower + rangeWidth * lowerBound символа
                    lower = lower.add(rangeWidth.multiply(symbolRange.lowerBound));
                    // Отмечаем, что символ найден
                    symbolFound = true;
                    // Переходим к следующему символу
                    break;
                }
            }
        }
    }

    //Метод для запуска программы
    public static void main(String[] args) {
        // Исходный текст для кодирования
        String text = "Он подошел к Анне Павловне, поцеловал ее руку, подставив ей свою надушенную и сияющую лысину, и покойно уселся на диване.\n" +
                "— Avant tout dites-moi, comment vous allez, chère amie?  Успокойте меня, — сказал он, не изменяя голоса и тоном, в котором из-за приличия и участия просвечивало равнодушие и даже насмешка.";
        // Записываем время начала выполнения
        long startTime = System.currentTimeMillis();
        // Создаем экземпляр класса ArithmeticCoding и инициализируем его с текстом
        ArithmeticCoding ac = new ArithmeticCoding(text);
        // Кодирование текста, получаем закодированное значение в виде дроби
        Fraction encoded = ac.encode(text);
        System.out.println("Закодированное значение: " + encoded);
        // Декодирование закодированного значения обратно в текст
        String decoded = ac.decode(encoded);
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
