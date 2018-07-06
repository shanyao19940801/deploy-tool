package com.yao.tool.ui;

import com.yao.tool.util.Constants;
import com.yao.tool.util.FieldUtil;
import com.yao.tool.util.FileUtils;
import com.yao.tool.util.Log;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.logging.Logger;

public class DeployUI extends JFrame{
    private JTextField tomcatFilePathTxt;
    private JTextField outCompileFilePathTxt;
    private JTextField backupFilePathTxt;
    private JLabel logFileLabel;
    private JLabel outCompileFileLabel;
    private JLabel backupLabel;
    private JButton logFilePathBtn;
    private JButton outCompileFilePathBtn;
    private JButton backupFilePathBtn;
    private JButton deployBtn;
    private JButton deployEndBtn;

    public DeployUI() {
        initComponents();
    }
    private void initComponents() {
        this.logFileLabel = new JLabel();
        this.tomcatFilePathTxt = new JTextField();
        this.outCompileFileLabel = new JLabel();
        this.outCompileFilePathTxt = new JTextField();
        this.backupLabel = new JLabel();
        this.backupFilePathTxt = new JTextField();
        this.logFilePathBtn = new JButton();
        this.outCompileFilePathBtn = new JButton();
        this.backupFilePathBtn = new JButton();
        this.deployBtn = new JButton();
        this.deployEndBtn = new JButton();

        setDefaultCloseOperation(3);
        setTitle("项目升级工具");
        setMinimumSize(new java.awt.Dimension(550, 230));
        setResizable(false);
        getContentPane().setLayout(null);

        this.logFileLabel.setText("部署目录：");
        getContentPane().add(this.logFileLabel);
        this.logFileLabel.setBounds(24, 14, 84, 15);

        this.tomcatFilePathTxt.setCursor(new java.awt.Cursor(2));
        getContentPane().add(this.tomcatFilePathTxt);
        this.tomcatFilePathTxt.setBounds(126, 11, 354, 21);


        //编译输出目录
        this.outCompileFileLabel.setText("发布包目录：");
        getContentPane().add(this.outCompileFileLabel);
        this.outCompileFileLabel.setBounds(24, 55, 84, 15);

        this.outCompileFilePathTxt.setCursor(new java.awt.Cursor(2));
        getContentPane().add(this.outCompileFilePathTxt);
        this.outCompileFilePathTxt.setBounds(126, 52, 354, 21);

        //发布目录
        this.backupLabel.setText("备份目录：");
        getContentPane().add(this.backupLabel);
        this.backupLabel.setBounds(24, 96, 84, 15);

        this.backupFilePathTxt.setCursor(new java.awt.Cursor(2));
        getContentPane().add(this.backupFilePathTxt);
        this.backupFilePathTxt.setBounds(126, 93, 354, 21);

        this.logFilePathBtn.setText("...");
        this.logFilePathBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                DeployUI.this.logFilePathBtnActionPerformed(evt);
            }
        });
        getContentPane().add(this.logFilePathBtn);
        this.logFilePathBtn.setBounds(490, 10, 21, 23);

        this.outCompileFilePathBtn.setText("...");
        this.outCompileFilePathBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                DeployUI.this.outCompileFilePathBtnActionPerformed(evt);
            }
        });
        getContentPane().add(this.outCompileFilePathBtn);
        this.outCompileFilePathBtn.setBounds(490, 51, 21, 23);

        this.backupFilePathBtn.setText("...");
        this.backupFilePathBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                DeployUI.this.deployFilePathBtnActionPerformed(evt);
            }
        });
        getContentPane().add(this.backupFilePathBtn);
        this.backupFilePathBtn.setBounds(490, 92, 21, 23);

        this.deployBtn.setText("开始升级");
        this.deployBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                DeployUI.this.deployBtnActionPerformed(evt);
            }
        });
        getContentPane().add(this.deployBtn);
        this.deployBtn.setBounds(230, 140, 100, 23);

        /*this.deployEndBtn.setText("部署完毕");
        this.deployEndBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                DeployUI.this.deployEndBtnActionPerformed(evt);
            }

        });
        getContentPane().add(this.deployEndBtn);
        this.deployEndBtn.setBounds(280, 140, 80, 23);*/
        pack();
    }

    public static void main(String[] args)
    {
        new DeployUI().setVisible(true);
    }

    private void deployEndBtnActionPerformed(ActionEvent evt) {
        DeployEndUI ui = new DeployEndUI(this.tomcatFilePathTxt.getText().trim());
        ui.setVisible(true);
    }

    private void deployFilePathBtnActionPerformed(ActionEvent evt) {
        fileChooser(evt, this.backupFilePathTxt, 1);
    }

    private void fileChooser(ActionEvent evt, JTextField textFiled, int mode)
    {
        JFileChooser fileChooser = new JFileChooser();

        if (!FieldUtil.isBlank(textFiled)) {
            fileChooser.setCurrentDirectory(new File(textFiled.getText()));
        }

        fileChooser.setFileSelectionMode(mode);
        if (fileChooser.showOpenDialog(this) == 0) {
            textFiled.setText(fileChooser.getSelectedFile().getAbsolutePath());
        }
    }

    //处理按钮
    private void deployBtnActionPerformed(ActionEvent evt) {
        if (checkFiled()) {
            try {
                Constants.logPath = this.backupFilePathTxt.getText();
                this.deployBtn.setVisible(false);

                //获取发布包所有文件目录
                List<String> filePaths = FileUtils.traverseFolder(this.outCompileFilePathTxt.getText());
                //将发布包中所有文件路劲写入到code.txt中
                FileUtils.writePathToFile(filePaths,this.backupFilePathTxt.getText());
                Log.INFO("----开始备份----");
                FileUtils.backupFile(this.tomcatFilePathTxt.getText(), this.backupFilePathTxt.getText(), this.backupFilePathTxt.getText());
                Log.INFO("----备份完成----");

                //部署
                Log.INFO("----开始部署----");
                String temp = FileUtils.deployFile(this.outCompileFilePathTxt.getText(), this.tomcatFilePathTxt.getText(),this.backupFilePathTxt.getText());
                Log.INFO("----部署结束----");
                if ("".equals(temp)) {
                    temp = "处理完成......！";
                }

                JOptionPane.showMessageDialog(null, temp, "提示", 1);

                File file = new File(this.backupFilePathTxt.getText());
                if (file.exists()) {
                    java.awt.Desktop.getDesktop().open(file);
                }
            } catch (FileNotFoundException ex) {
                this.deployBtn.setVisible(true);
                Logger.getLogger(DeployUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "提示", 1);
            }
            catch (java.io.IOException ex) {
                this.deployBtn.setVisible(true);
                Logger.getLogger(DeployUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "提示", 1);
            }
            finally {
                this.deployBtn.setVisible(true);
            }
        }
    }

    private boolean checkFiled()
    {
        if (FieldUtil.isBlank(this.tomcatFilePathTxt)) {
            JOptionPane.showMessageDialog(null, "请选择部署目录.......！", "提示", 1);

            return false;
        }
        if (FieldUtil.isBlank(this.outCompileFilePathTxt)) {
            JOptionPane.showMessageDialog(null, "请选择发布包目录.......！", "提示", 1);

            return false;
        }
        if (FieldUtil.isBlank(this.backupFilePathTxt)) {
            JOptionPane.showMessageDialog(null, "请选择备份目录.......！", "提示", 1);

            return false;
        }
        return true;
    }

    private void logFilePathBtnActionPerformed(ActionEvent evt) {
        fileChooser(evt, this.tomcatFilePathTxt, 1);
    }

    private void outCompileFilePathBtnActionPerformed(ActionEvent evt) {
        fileChooser(evt, this.outCompileFilePathTxt, 1);
    }

}
