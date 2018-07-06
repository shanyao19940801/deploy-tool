package com.yao.tool.util;

import javax.swing.*;
import java.awt.*;

public class GuiTools
{
    public static void makeCenter(JDialog dialog)
    {
        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int height = Toolkit.getDefaultToolkit().getScreenSize().height;

        dialog.setLocation(width / 2 - dialog.getWidth() / 2, height / 2 - dialog.getHeight() / 2);
    }






    public static void makeCenter(JFrame frame)
    {
        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int height = Toolkit.getDefaultToolkit().getScreenSize().height;

        frame.setLocation(width / 2 - frame.getWidth() / 2, height / 2 - frame.getHeight() / 2);
    }



    public static void setWindowsLookAndFeel(JFrame frame)
    {
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(frame);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void setWindowsLookAndFeel(JDialog dialog)
    {
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(dialog);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
