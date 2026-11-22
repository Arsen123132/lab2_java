# Лабораторна робота 2: XML, XSD, SAX/DOM/StAX, XSLT

Варіант 5: **Комп'ютери** (кореневий елемент `Device`).

## Опис

Консольний Java-застосунок, який:

- розбирає XML-файл із комплектуючими комп'ютера трьома парсерами: DOM, SAX і StAX;
- заповнює колекцію об'єктів `Device` даними з XML;
- сортує колекцію через `Comparator`;
- перевіряє XML за схемою XSD;
- за допомогою XSL перетворює XML у файл зі зміненою структурою (комплектуючі згруповано за `Group`).

## Вимоги

- JDK 17+
- Maven 3.8+

## Структура проєкту

```
src/main/java/ua/lab2/devices/   вихідний код
src/main/resources/
├── xml/devices.xml              вхідний XML-документ
├── xsd/devices.xsd              XSD-схема
├── xsl/                         XSL-перетворення
├── app.properties               параметри ініціалізації
└── log4j2.xml                   налаштування логування
src/test/java/ua/lab2/devices/   unit-тести
```

## Збірка і тести

```bash
mvn clean verify
```

Команда запускає тести й перевіряє, що покриття коду становить 100%.
Звіт: `target/site/jacoco/index.html`.

## Запуск

```bash
mvn compile exec:java
```

Шляхи до файлів налаштовуються в `src/main/resources/app.properties`.
Результат перетворення зберігається в `output/devices-by-group.xml`,
логи пишуться в `logs/app.log`.
## Стек

Java 17, Maven, JUnit 5, Mockito, Log4j2, JaCoCo.