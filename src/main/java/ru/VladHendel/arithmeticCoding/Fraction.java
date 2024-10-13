package ru.VladHendel.arithmeticCoding;

import java.math.BigInteger;


//Класс-реализация дроби
class Fraction implements Comparable<Fraction> {
    private BigInteger numerator;    // Числитель
    private BigInteger denominator;  // Знаменатель



    // Конструкторы
    // Конструктор, принимающий числитель и знаменатель типа long.
    public Fraction(long numerator, long denominator) {
        this(BigInteger.valueOf(numerator), BigInteger.valueOf(denominator));
    }

    //Конструктор, принимающий числитель и знаменатель типа BigInteger.
    public Fraction(BigInteger numerator, BigInteger denominator) {
        // Инициализируем числитель и знаменатель
        this.numerator = numerator;
        this.denominator = denominator;
        // Упрощаем дробь
        simplify();
    }

    // Упрощение дроби
    private void simplify() {
        // Находим наибольший общий делитель числителя и знаменателя
        BigInteger gcd = numerator.gcd(denominator);
        // Делим числитель и знаменатель на НОД
        numerator = numerator.divide(gcd);
        denominator = denominator.divide(gcd);
        // Знаменатель должен быть положительным. Если знаменатель отрицательный, меняем знаки числителя и знаменателя
        if (denominator.compareTo(BigInteger.ZERO) < 0) {
            numerator = numerator.negate();
            denominator = denominator.negate();
        }
    }

    // Умножение дроби на другую дробь
    public Fraction multiply(Fraction other) {
        // Умножаем числители и знаменатели соответственно
        return new Fraction(numerator.multiply(other.numerator), denominator.multiply(other.denominator));
    }

    // Деление дроби на другую дробь
    public Fraction divide(Fraction other) {
        // Проверяем, что делитель не равен нулю
        if (other.numerator.equals(BigInteger.ZERO)) {
            throw new ArithmeticException("Нельзя делить на ноль");
        }
        // Умножаем на обратную дробь
        return new Fraction(numerator.multiply(other.denominator), denominator.multiply(other.numerator));
    }

    // Сложение дроби с другой дробью
    public Fraction add(Fraction other) {
        // Вычисляем новый числитель и знаменатель, приводя к общему знаменателю
        BigInteger newNumerator = numerator.multiply(other.denominator).add(other.numerator.multiply(denominator));
        BigInteger newDenominator = denominator.multiply(other.denominator);
        // Возвращаем новую дробь
        return new Fraction(newNumerator, newDenominator);
    }

    // Вычитание другой дроби из текущей дроби
    public Fraction subtract(Fraction other) {
        // Вычисляем новый числитель и знаменатель, приводя к общему знаменателю
        BigInteger newNumerator = numerator.multiply(other.denominator).subtract(other.numerator.multiply(denominator));
        BigInteger newDenominator = denominator.multiply(other.denominator);
        // Возвращаем новую дробь
        return new Fraction(newNumerator, newDenominator);
    }

    // Метод для сравнения дробей
    @Override
    public int compareTo(Fraction other) {
        // Сравниваем дроби путем сравнения числителей после приведения к общему знаменателю
        // Вычисляем leftSide = this.numerator * other.denominator
        BigInteger leftSide = numerator.multiply(other.denominator);
        // Вычисляем rightSide = other.numerator * this.denominator
        BigInteger rightSide = other.numerator.multiply(denominator);
        // Сравниваем leftSide и rightSide
        // Если leftSide < rightSide, возвращаем -1
        // Если leftSide == rightSide, возвращаем 0
        // Если leftSide > rightSide, возвращаем 1
        return leftSide.compareTo(rightSide);
    }

    // Преобразование дроби в строку
    @Override
    public String toString() {
        return numerator + "/" + denominator;
    }
}