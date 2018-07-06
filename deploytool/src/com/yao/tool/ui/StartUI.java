package com.yao.tool.ui;

import javax.swing.*;

public class StartUI extends JFrame {
    private JButton deployBtn;
    private JButton updateBtn;

    public StartUI() {
        this.deployBtn = new JButton();
        this.updateBtn = new JButton();

        setDefaultCloseOperation(3);
        setTitle("项目升级工具");
        setMinimumSize(new java.awt.Dimension(550, 230));
        setResizable(false);
        getContentPane().setLayout(null);

    }

}
