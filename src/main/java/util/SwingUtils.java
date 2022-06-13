package util;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;


/**
 * Набор функций для работы с оконными приложениями с ипользованием Swing
 *
 * @author Дмитрий Соломатин (кафедра ПиИТ ФКН ВГУ)
 */
public class SwingUtils {
    /**
     * Показать диалоговое окно с сообщением об ошибке
     *
     * @param message Сообщение
     * @param title   Заголовок окна
     * @param ex      Иcключение
     */
    public static void showErrorMessageBox(String message, String title, Throwable ex) {
        StringBuilder sb = new StringBuilder(ex.toString());
        if (message != null) {
            sb.append(message);
        }
        sb.append("\n");
        for (StackTraceElement ste : ex.getStackTrace()) {
            sb.append("\n\tat ");
            sb.append(ste);
        }
        JOptionPane.showMessageDialog(null, sb.toString(), title, JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Показать диалоговое окно с сообщением об ошибке
     *
     * @param message Сообщение
     * @param ex      Иcключение
     */
    public static void showErrorMessageBox(String message, Throwable ex) {
        showErrorMessageBox(message, "Ошибка", ex);
    }

    /**
     * Показать диалоговое окно с сообщением об ошибке
     *
     * @param ex Иcключение
     */
    public static void showErrorMessageBox(Throwable ex) {
        showErrorMessageBox(null, ex);
    }

    /**
     * Установка шрифта по умолчанию для элементов управления
     *
     * @param fontName Название шрифта (null - не менять)
     * @param size     Размер шрифта (меньше или равно 0 - не менять)
     */
    public static void setDefaultFont(String fontName, int size) {
        UIManager.getDefaults().forEach((key, value1) -> {
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource fr) {
                fr = new FontUIResource(
                        (fontName != null) ? fontName : fr.getFontName(),
                        fr.getStyle(),
                        (size > 0) ? size : fr.getSize()
                );
                UIManager.put(key, fr);
            }
        });
    }
}