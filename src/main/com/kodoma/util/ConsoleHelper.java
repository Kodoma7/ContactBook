package main.com.kodoma.util;

import main.com.kodoma.exceptions.WrongPhoneFormat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Кодома on 11.07.2017.
 */
public class ConsoleHelper {
    private static BufferedReader bis = new BufferedReader(new InputStreamReader(System.in));

    public static void writeMessage(String message) {
        System.out.println(message);
    }

    public static String readString() throws IOException {
        String text = bis.readLine();
        return text;
    }

    public static int readInt() throws IOException {
        String text = readString();
        return Integer.parseInt(text.trim());
    }

    public static long readLong() throws IOException {
        String text = readString();
        return Long.parseLong(text);
    }

    public static int readPhoneNumber() throws IOException {
        int number;
        try {
            number = readInt();
        } catch (NumberFormatException e) {
            throw new WrongPhoneFormat();
        }
        return number;
    }

    public static void initMenu() {
        StringBuilder sb = new StringBuilder();
        sb.append("-------------Menu-------------\n");
        sb.append(" 1 -> Создать новый контакт\n");
        sb.append(" 2 -> Редактировать контакт\n");
        sb.append(" 3 -> Удалить контакт\n");
        sb.append(" 4 -> Добавить контакт в группу\n");
        sb.append(" 5 -> Удалить контакт из группы\n");
        sb.append(" 6 -> Просмотр информации о контакте\n");
        sb.append(" 7 -> Просмотр всех контактов\n");
        sb.append(" 8 -> Просмотр контактов выбранной группы\n");
        sb.append(" 9 -> Просмотр всех групп\n");
        sb.append("10 -> Удаление группы\n");
        sb.append("11 -> Редактирование группы\n");
        sb.append("12 -> Сохранить список контактов\n");
        sb.append("13 -> Загрузить список контактов\n");
        sb.append(" 0 -> Выход\n");

        writeMessage(sb.toString());
    }
}
