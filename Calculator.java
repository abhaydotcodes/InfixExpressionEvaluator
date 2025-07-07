import java.io.*;
import java.util.*;
public class Calculator {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter your mathematical expression: ");
        String exp = sc.nextLine().replaceAll(" ", "");
        try {
            double result = Simplify(exp);
            System.out.println("Result: " + result);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    public static double Simplify(String exp) {
        Stack<Character> operators = new Stack<>();
        Stack<Double> operands = new Stack<>();
        int l = exp.length();

        for (int i = 0; i < l; i++) {
            char ch = exp.charAt(i);
            if (Character.isDigit(ch) || ch == '.') {
                String num = "";
                while (i < l && (Character.isDigit(exp.charAt(i)) || exp.charAt(i) == '.')) {
                    num += exp.charAt(i);
                    i++;
                }
                try {
                    operands.push(Double.parseDouble(num));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid number: " + num);
                }
                i--; // backtrack after number read
            } else if (ch == '(') {
                operators.push(ch);
            } else if (ch == '+' || ch == '-' || ch == '*' || ch == '/') {
                while (!operators.isEmpty() && operators.peek() != '(' &&
                        precedence(operators.peek()) >= precedence(ch)) {
                    evaluate(operands, operators);
                }
                operators.push(ch);
            } else if (ch == ')') {
                while (!operators.isEmpty() && operators.peek() != '(') {
                    evaluate(operands, operators);
                }
                if (!operators.isEmpty() && operators.peek() == '(') {
                    operators.pop();
                } else {
                    throw new IllegalArgumentException("Mismatched parentheses");
                }
            } else {
                throw new IllegalArgumentException("Invalid character in expression: " + ch);
            }
        }
        while (!operators.isEmpty()) {
            evaluate(operands, operators);
        }

        if (operands.size() != 1) {
            throw new IllegalArgumentException("Invalid expression.");
        }
        return operands.pop();
    }
    public static int precedence(char ch) {
        if (ch == '/' || ch == '*') return 2;
        else if (ch == '+' || ch == '-') return 1;
        return 0;
    }
    public static void evaluate(Stack<Double> operands, Stack<Character> operators) {
        char op = operators.pop();
        double b = operands.pop();
        double a = operands.pop();
        double ans;

        switch (op) {
            case '+': ans = a + b; break;
            case '-': ans = a - b; break;
            case '*': ans = a * b; break;
            case '/':
                if (b == 0.0) throw new ArithmeticException("Division by zero!");
                ans = a / b;
                break;
            default:
                throw new IllegalArgumentException("Unknown operator: " + op);
        }
        operands.push(ans);
    }
}
