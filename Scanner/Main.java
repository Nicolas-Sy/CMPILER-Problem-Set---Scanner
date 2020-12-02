import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        try {
            File file = new File("input.txt");
            Lexer lexerClass = new Lexer(file);
            List<String> tokenList = lexerClass.generateTokens();
            FileWriter myWriter = new FileWriter("output.txt");
            PrintWriter printWriter = new PrintWriter(myWriter);
            for (int i = 0; i < tokenList.size(); i++) {
                if(tokenList.get(i).toString() == "NEWLINE"){
                    printWriter.print("\n");
                    continue;
                }
                printWriter.print(tokenList.get(i).toString() + " ");
            }
            printWriter.close();
            myWriter.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
