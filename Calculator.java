import java.io.*;
import java.util.*;

public class Calculator{
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter your mathematical expression: ");
        String exp = sc.nextLine().replaceAll(" ", "");
        try {
            int result = Simplify(exp);
            System.out.println("Result: " + result);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static int Simplify(String exp) {
        Stack<Character> operators = new Stack<>();
        Stack<Integer> operands = new Stack<>();
        int l = exp.length();

        for (int i = 0; i < l; i++) {
            char ch = exp.charAt(i);

            if (Character.isDigit(ch)) {
                int num = 0;
                while (i < l && Character.isDigit(exp.charAt(i))) {
                    num = num * 10 + (exp.charAt(i) - '0');
                    i++;
                }
                operands.push(num);
                i--; // step back for loop increment
            } else if (ch == '(') {
                operators.push(ch);
            } else if (ch == '+' || ch == '-' || ch == 'x' || ch == '/') {
                while (!operators.isEmpty() && operators.peek() != '(' && precedence(operators.peek()) >= precedence(ch)) {
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
        if (ch == '/' || ch == 'x') return 2;
        else if (ch == '+' || ch == '-') return 1;
        return 0;
    }

    public static void evaluate(Stack<Integer> operands, Stack<Character> operators) {
        char op = operators.pop();
        int b = operands.pop();
        int a = operands.pop();
        int ans;

        switch (op) {
            case '+': ans = a + b; break;
            case '-': ans = a - b; break;
            case 'x': ans = a * b; break;
            case '/':
                if (b == 0) throw new ArithmeticException("Division by zero!");
                ans = a / b;
                break;
            default:
                throw new IllegalArgumentException("Unknown operator: " + op);
        }
        operands.push(ans);
    }
}
