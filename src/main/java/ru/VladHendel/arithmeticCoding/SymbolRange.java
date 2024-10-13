package ru.VladHendel.arithmeticCoding;

//Класс для представления интервала символа в арифметическом кодировании.
class SymbolRange {
    char symbol; // Символ, которому соответствует интервал
    Fraction lowerBound; // Нижняя граница интервала
    Fraction upperBound; // Верхняя граница интервала

    // Конструктор класса SymbolRange.
    public SymbolRange(char symbol, Fraction lowerBound, Fraction upperBound) {
        this.symbol = symbol;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    // Метод проверки, попадает ли значение в диапазон символа
    public boolean isInRange(Fraction value) {
        // Проверяем, что value >= lowerBound и value < upperBound
        return value.compareTo(lowerBound) >= 0 && value.compareTo(upperBound) < 0;
    }
}