package gui;

import constants.CommonConstants;
import service.CalculatorService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CalculatorGUI extends JFrame implements ActionListener {
    private final SpringLayout springLayout = new SpringLayout();

    private CalculatorService calculateService;
    private JTextField displayfield;

    private JButton[] buttons;

    //flags
    private boolean pressedOperator = false;
    private boolean pressedEquals = false;


    public CalculatorGUI() {
        super(CommonConstants.APP_NAME);
        setSize(CommonConstants.APP_SIZE[0], CommonConstants.APP_SIZE[1]);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(springLayout);

        calculateService=new CalculatorService();

        addGuiComponents();

    }

    public void addGuiComponents() {
        //add display components
        addDisplayFieldComponents();
        //add button components
        addButtonComponents();
    }

    public void addDisplayFieldComponents() {
        JPanel displayfieldpanel = new JPanel();
        displayfield = new JTextField(CommonConstants.TEXTFIELD_LENGTH);
        displayfield.setFont(new Font("Dialog", Font.PLAIN, CommonConstants.TEXTFIELD_FONTSIZE));
        displayfield.setEditable(false);
        displayfield.setText("0");
        displayfield.setHorizontalAlignment(SwingConstants.RIGHT);

        displayfieldpanel.add(displayfield);

        this.getContentPane().add(displayfieldpanel);
        springLayout.putConstraint(SpringLayout.NORTH, displayfieldpanel, CommonConstants.TEXTFIELD_SPRINGLAYOUT_NORTHPAD, SpringLayout.NORTH, this);
        springLayout.putConstraint(SpringLayout.WEST, displayfieldpanel, CommonConstants.TEXTFIELD_SPRINGLAYOUT_WESTPAD, SpringLayout.WEST, this);
    }

    public void addButtonComponents() {
        GridLayout gridLayout = new GridLayout(CommonConstants.BUTTON_ROWCOUNT, CommonConstants.BUTTON_COLCOUNT);
        JPanel buttonpanel = new JPanel();
        buttonpanel.setLayout(gridLayout);
        buttons = new JButton[CommonConstants.BUTTON_COUNT];
        for (int i = 0; i < CommonConstants.BUTTON_COUNT; i++) {
            JButton button = new JButton(getButtonLabel(i));
            button.setFont(new Font("Dialog", Font.PLAIN, CommonConstants.BUTTON_FONTSIZE));
            button.addActionListener(this);

            buttonpanel.add(button);

        }
        gridLayout.setHgap(CommonConstants.BUTTON_HGAP);
        gridLayout.setVgap(CommonConstants.BUTTON_VGAP);
        this.getContentPane().add(buttonpanel);

        springLayout.putConstraint(SpringLayout.NORTH, buttonpanel, CommonConstants.BUTTON_SPRINGLAYOUT_NORTHPAD, SpringLayout.NORTH, this);
        springLayout.putConstraint(SpringLayout.WEST, buttonpanel, CommonConstants.BUTTON_SPRINGLAYOUT_WESTPAD, SpringLayout.WEST, this);
    }

    public String getButtonLabel(int buttonIndex) {
        switch (buttonIndex) {
            case 0:
                return "7";
            case 1:
                return "8";
            case 2:
                return "9";
            case 3:
                return "/";
            case 4:
                return "4";
            case 5:
                return "5";
            case 6:
                return "6";
            case 7:
                return "x";
            case 8:
                return "1";
            case 9:
                return "2";
            case 10:
                return "3";
            case 11:
                return "-";
            case 12:
                return "0";
            case 13:
                return ".";
            case 14:
                return "+";
            case 15:
                return "=";

        }
        return "";
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String buttoncommand = e.getActionCommand();
        if (buttoncommand.matches("[0-9]")) {
            if (pressedEquals || pressedOperator || displayfield.getText().equals("0")) {
                displayfield.setText(buttoncommand);
            } else
                displayfield.setText(displayfield.getText() + buttoncommand);
        //update flags
        pressedOperator = false;
        pressedEquals = false;

        }
        else if(buttoncommand.equals("=")){
            //calculate
            calculateService.setNum2(Double.parseDouble(displayfield.getText()));

            double result = 0;
            switch (calculateService.getMathSymbol()){
                case '+':
                    calculateService.add();
                    break;
                case '-':
                    calculateService.subtract();
                    break;
                case 'x':
                    calculateService.multiply();
                    break;
                case '/':
                    calculateService.divide();
                    break;


            }
            //update the display field
            displayfield.setText(Double.toString(result));


            //update flags
            pressedEquals=true;
            pressedOperator=false;

        } else if (buttoncommand.equals(".")) {
            if(!displayfield.getText().contains(".")){
                displayfield.setText(displayfield.getText()+buttoncommand);
            }
            else{
                if(!pressedOperator)
                    calculateService.setNum1(Double.parseDouble(displayfield.getText()));

                calculateService.setMathSymbol(buttoncommand.charAt(0));

                //update flags
                pressedOperator=true;
                pressedEquals=false;
            }

        }
    }
}

