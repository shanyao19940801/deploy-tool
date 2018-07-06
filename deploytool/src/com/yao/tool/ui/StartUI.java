package com.yao.tool.ui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartUI extends JFrame {
    private JButton deployBtn;
    private JButton updateBtn;

    public StartUI() {
        this.deployBtn = new JButton();
        this.updateBtn = new JButton();

        setDefaultCloseOperation(3);
        setTitle("项目升级工具");
        setMinimumSize(new java.awt.Dimension(400, 120));
        setResizable(false);
        getContentPane().setLayout(null);

        this.deployBtn.setText("生成部署包");
        this.deployBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                StartUI.this.showDeployPackageUI(evt);
            }
        });

        getContentPane().add(this.deployBtn);
        this.deployBtn.setBounds(100, 140, 100, 23);


        this.updateBtn.setText("升级");
        this.updateBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StartUI.this.showDeployUI();
            }
        });
        getContentPane().add(this.updateBtn);
        this.updateBtn.setBounds(200, 140, 100, 23);
    }

    private void showDeployPackageUI(ActionEvent evt) {
        DeployPackageUI deployPackageUI = new DeployPackageUI();
//        deployPackageUI.
    }

    private void showDeployUI() {
        DeployUI ui = new DeployUI();
        ui.setVisible(true);
    }

    public static void main(String[] args) {
        new StartUI().setVisible(true);
    }

}
