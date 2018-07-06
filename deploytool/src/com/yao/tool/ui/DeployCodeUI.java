package com.yao.tool.ui;

import com.yao.tool.util.FieldUtil;
import com.yao.tool.util.FileUtils;
import com.yao.tool.util.GuiTools;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;


public class DeployCodeUI extends javax.swing.JFrame {
    private JTextField logFilePathTxt;
    private JTextField outCompileFilePathTxt;
    private JTextField deployFilePathTxt;
    private JLabel logFileLabel;
    private JLabel outCompileFileLabel;
    private JLabel deployLabel;
    private JButton logFilePathBtn;
    private JButton outCompileFilePathBtn;
    private JButton deployFilePathBtn;
    private JButton deployBtn;
    private JButton deployEndBtn;
    private JCheckBox chkSuorceFiles;

    public DeployCodeUI() {
        initComponents();
        GuiTools.setWindowsLookAndFeel(this);
        GuiTools.makeCenter(this);
    }

    private void initComponents() {
        this.logFileLabel = new JLabel();
        this.logFilePathTxt = new JTextField();
        this.outCompileFileLabel = new JLabel();
        this.outCompileFilePathTxt = new JTextField();
        this.deployLabel = new JLabel();
        this.deployFilePathTxt = new JTextField();
        this.logFilePathBtn = new JButton();
        this.outCompileFilePathBtn = new JButton();
        this.deployFilePathBtn = new JButton();
        this.deployBtn = new JButton();
        this.deployEndBtn = new JButton();
        this.chkSuorceFiles = new JCheckBox();

//        setDefaultCloseOperation(3);
        setTitle("项目部署工具");
        setMinimumSize(new java.awt.Dimension(550, 230));
        setResizable(false);
        getContentPane().setLayout(null);

        this.logFileLabel.setText("日志文件目录：");
        getContentPane().add(this.logFileLabel);
        this.logFileLabel.setBounds(24, 14, 84, 15);

        this.logFilePathTxt.setCursor(new java.awt.Cursor(2));
        getContentPane().add(this.logFilePathTxt);
        this.logFilePathTxt.setBounds(126, 11, 354, 21);

        this.chkSuorceFiles.setText("复制源代码");
        getContentPane().add(this.chkSuorceFiles);
        this.chkSuorceFiles.setBounds(125, 32, 300, 15);

        this.outCompileFileLabel.setText("编译输出目录：");
        getContentPane().add(this.outCompileFileLabel);
        this.outCompileFileLabel.setBounds(24, 55, 84, 15);

        this.outCompileFilePathTxt.setCursor(new java.awt.Cursor(2));
        getContentPane().add(this.outCompileFilePathTxt);
        this.outCompileFilePathTxt.setBounds(126, 52, 354, 21);

        this.deployLabel.setText("发布目录：");
        getContentPane().add(this.deployLabel);
        this.deployLabel.setBounds(24, 96, 84, 15);

        this.deployFilePathTxt.setCursor(new java.awt.Cursor(2));
        getContentPane().add(this.deployFilePathTxt);
        this.deployFilePathTxt.setBounds(126, 93, 354, 21);

        this.logFilePathBtn.setText("...");
        this.logFilePathBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                DeployCodeUI.this.logFilePathBtnActionPerformed(evt);
            }
        });
        getContentPane().add(this.logFilePathBtn);
        this.logFilePathBtn.setBounds(490, 10, 21, 23);

        this.outCompileFilePathBtn.setText("...");
        this.outCompileFilePathBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                DeployCodeUI.this.outCompileFilePathBtnActionPerformed(evt);
            }
        });
        getContentPane().add(this.outCompileFilePathBtn);
        this.outCompileFilePathBtn.setBounds(490, 51, 21, 23);

        this.deployFilePathBtn.setText("...");
        this.deployFilePathBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                DeployCodeUI.this.deployFilePathBtnActionPerformed(evt);
            }
        });
        getContentPane().add(this.deployFilePathBtn);
        this.deployFilePathBtn.setBounds(490, 92, 21, 23);

        this.deployBtn.setText("处理");
        this.deployBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                DeployCodeUI.this.deployBtnActionPerformed(evt);
            }
        });
        getContentPane().add(this.deployBtn);
        this.deployBtn.setBounds(200, 140, 60, 23);

        this.deployEndBtn.setText("部署完毕");
        this.deployEndBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                DeployCodeUI.this.deployEndBtnActionPerformed(evt);
            }

        });
        getContentPane().add(this.deployEndBtn);
        this.deployEndBtn.setBounds(280, 140, 80, 23);
        pack();
    }

    private void deployFilePathBtnActionPerformed(ActionEvent evt) {
        fileChooser(evt, this.deployFilePathTxt, 1);
    }

    private void logFilePathBtnActionPerformed(ActionEvent evt) {
        fileChooser(evt, this.logFilePathTxt, 1);
    }

    private void outCompileFilePathBtnActionPerformed(ActionEvent evt) {
        fileChooser(evt, this.outCompileFilePathTxt, 1);
    }

    private void deployEndBtnActionPerformed(ActionEvent evt) {
        DeployEndUI ui = new DeployEndUI(this.logFilePathTxt.getText().trim());
        ui.setVisible(true);
    }

    private void deployBtnActionPerformed(ActionEvent evt) {
        if (checkFiled()) {
            try {
                this.deployBtn.setVisible(false);
                File codeFile = null;
                File tCodeFile = new File(this.logFilePathTxt.getText());
                String codePath = "";
                if (tCodeFile.isDirectory()) {
                    codePath = this.logFilePathTxt.getText() + File.separator + "code.txt";
                    codeFile = new File(FileUtils.enCoding(codePath));
                    if ((codeFile == null) || (!codeFile.exists())) {
                        throw new FileNotFoundException("日志文件路径中必须包含code.txt这个发布文件。");
                    }
                } else {
                    codePath = this.logFilePathTxt.getText();
                    codeFile = tCodeFile;
                }


                FileUtils.copyFilesAndDirForDeploy(this.logFilePathTxt.getText(), this.deployFilePathTxt.getText());


                String temp = FileUtils.deployFileDeploy(codeFile, this.outCompileFilePathTxt.getText(), this.deployFilePathTxt.getText(), this.chkSuorceFiles.isSelected());

                if ("".equals(temp)) {
                    temp = "处理完成......！";
                }

                JOptionPane.showMessageDialog(null, temp, "提示", 1);

                File file = new File(this.deployFilePathTxt.getText());
                if (file.exists()) {
                    java.awt.Desktop.getDesktop().open(file);
                }
            } catch (FileNotFoundException ex) {
                this.deployBtn.setVisible(true);
                Logger.getLogger(DeployCodeUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "提示", 1);
            } catch (java.io.IOException ex) {
                this.deployBtn.setVisible(true);
                Logger.getLogger(DeployCodeUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "提示", 1);
            } finally {
                this.deployBtn.setVisible(true);
            }
        }
    }

    private void fileChooser(ActionEvent evt, JTextField textFiled, int mode) {
        JFileChooser fileChooser = new JFileChooser();

        if (!FieldUtil.isBlank(textFiled)) {
            fileChooser.setCurrentDirectory(new File(textFiled.getText()));
        }

        fileChooser.setFileSelectionMode(mode);
        if (fileChooser.showOpenDialog(this) == 0) {
            textFiled.setText(fileChooser.getSelectedFile().getAbsolutePath());
        }
    }

    public static void main(String[] args) {
        new DeployCodeUI().setVisible(true);
    }

    private boolean checkFiled() {
        if (FieldUtil.isBlank(this.logFilePathTxt)) {
            JOptionPane.showMessageDialog(null, "请选择日志文件目录.......！", "提示", 1);

            return false;
        }
        if (FieldUtil.isBlank(this.outCompileFilePathTxt)) {
            JOptionPane.showMessageDialog(null, "请选择编译输出目录.......！", "提示", 1);

            return false;
        }
        if (FieldUtil.isBlank(this.deployFilePathTxt)) {
            JOptionPane.showMessageDialog(null, "请选择发布目录.......！", "提示", 1);

            return false;
        }
        return true;
    }
}