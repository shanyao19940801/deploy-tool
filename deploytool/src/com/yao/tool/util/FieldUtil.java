package com.yao.tool.util;

import javax.swing.*;

public class FieldUtil {
    public static boolean isBlank(JTextField field)
    {
        if ((field.getText() == null) || ("".equals(field.getText().trim()))) {
            return true;
        }
        return false;
    }
}
