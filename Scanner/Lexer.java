import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Lexer{

    enum Token{
        GPR, 
        FPR, 
        KEYWORD, 
        ERROR   
    }
    
    BufferedReader reader;
    char current;

    char readNextChar() {
        try {
            return (char) reader.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (char) (-1);
    }

    boolean zeroFourToNine(char c) {
        if (c == '0' || c == '4' || c == '5' || c == '6' || c == '7' || c == '8' || c == '9')
            return true;

        return false;
    }

    boolean three(char c) {
        if (c == '3')
            return true;

        return false;
    }

    boolean oneTwo(char c) {
        if (c == '1' || c == '2')
            return true;

        return false;
    }

    boolean zeroToNine(char c) {
        if (c == '0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7' || c == '8' || c == '9')
            return true;

        return false;
    }

    boolean zeroOrOne(char c) {
        if (c == '0' || c == '1')
            return true;

        return false;
    }

    boolean isRegister(String num){
        int registerValue = Integer.parseInt(num);
        if (registerValue >= 0 && registerValue <= 31)
            return true;

        return false;
    }

    public Lexer(File file) {
        try {
            reader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        current = readNextChar();
    }

    List<String> tokenList = new ArrayList<>();

    List<String> generateTokens() {
        String token = readNextToken();
        while (token != null) {
            tokenList.add(token);
            token = readNextToken();
        }
        return tokenList;
    }
    
    String readNextToken() {
        int state = 0;

        while (true) {
            if (current == (char) (-1)) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            switch (state) {
                case 0: {
                    if (current == '\n') 
                    {
                        current = readNextChar();
                        return "NEWLINE";
                    }
                    else if(current == ' ')
                    {
                        current = readNextChar();
                        continue;
                    }
                    else if(current == Character.LINE_SEPARATOR)
                    {
                        current = readNextChar();
                        continue;
                    }
                    else if(current == ',')
                    {
                        current = readNextChar();
                        continue;
                    }

                    else if(current == 'R'){
                        state = 1;
                        break;
                    }
                    else if(current == '$'){
                        state = 5;
                        break;
                    }
                    else if (current == 'F'){
                        state = 6;
                        break;
                    }
                    else if (current == 'D'){
                        state = 10;
                        break;
                    }
                    else{
                        state = 21;
                        break;
                    }
                }

                case 1: {
                    if(current == 'R'){
                        current = readNextChar();
                        if(zeroFourToNine(current)){
                            state = 2;
                            break;
                        }
                        else if(three(current)){
                            state = 3;
                            break;
                        }
                        else if(oneTwo(current)){
                            state = 4;
                            break;
                        }
                        else{
                            state = 21;
                            break;
                        }
                    }
                }

                case 2:{
                    if(zeroFourToNine(current)){
                        current = readNextChar();
                        if (current == Character.LINE_SEPARATOR || current == '\n' || current == ',' || current == ' ' || current == (char) (-1)) 
                            return Token.GPR.toString();                      

                        else{
                            state = 21;
                            break;
                        }                           
                    }
                }

                case 3:{
                    if(three(current)){
                        current = readNextChar();                      
                        if (current == Character.LINE_SEPARATOR || current == '\n' || current == ',' || current == ' ' || current == (char) (-1)) 
                            return Token.GPR.toString();

                        else if(zeroOrOne(current)){
                            current = readNextChar();
                            if(current != (char) (-1) && current != '\n' && current != ',' && current != ' ' && current != Character.LINE_SEPARATOR){
                                state = 21;
                                break;
                            }
                            else
                                return Token.GPR.toString(); 
                        }
                        else{
                            state = 21;
                            break;
                        }                                   
                    }
                }

                case 4:{
                    if(oneTwo(current)){
                        current = readNextChar();
                        if (current == Character.LINE_SEPARATOR || current == '\n' || current == ',' || current == ' ' || current == (char) (-1)) 
                            return Token.GPR.toString();
                        
                        else if(zeroToNine(current)){
                            current = readNextChar();
                            if(current != (char) (-1) && current != '\n' && current != ',' && current != ' ' && current != Character.LINE_SEPARATOR){
                                state = 21;
                                break;  
                            }
                            else
                                return Token.GPR.toString();
                        }
                        else{
                            state = 21;
                            break;
                        }    
                    }
                }

                case 5:{
                    if(current == '$'){
                        current = readNextChar();
                        if(current == 'F'){
                            state = 6;
                            break;
                        }
                        else if(zeroFourToNine(current)){
                            state = 2;
                            break;
                        }
                        else if(three(current)){
                            state = 3;
                            break;
                        }
                        else if(oneTwo(current)){
                            state = 4;
                            break;
                        }
                        else{
                            state = 21;
                            break;
                        }         
                    }                                 
                }

                case 6:{
                    if(current == 'F'){
                        current = readNextChar();
                        if(zeroFourToNine(current)){
                            state = 7;
                            break;
                        }
                        else if(three(current)){
                            state = 8;
                            break;
                        }
                        else if(oneTwo(current)){
                            state = 9;
                            break;
                        }
                        else{
                            state = 21;
                            break;
                        }
                    }
                }

                case 7:{
                    if(zeroFourToNine(current)){
                        current = readNextChar();
                        if (current == Character.LINE_SEPARATOR || current == '\n' || current == ',' || current == ' ' || current == (char) (-1)) 
                            return Token.FPR.toString(); 
                        else{
                            state = 21;
                            break;
                        }                           
                    }
                }

                case 8:{
                    if(three(current)){
                        current = readNextChar();                      
                        if (current == Character.LINE_SEPARATOR || current == '\n' || current == ',' || current == ' ' || current == (char) (-1)) 
                            return Token.FPR.toString();

                        else if(zeroOrOne(current)){
                            current = readNextChar();
                            if(current != (char) (-1) && current != '\n' && current != ',' && current != ' ' && current != Character.LINE_SEPARATOR){
                                state = 21;
                                break;
                            }
                            else
                                return Token.FPR.toString(); 
                        }
                        else{
                            state = 21;
                            break;
                        }                                   
                    }    
                }

                case 9:{
                    if(oneTwo(current)){
                        current = readNextChar();
                        if (current == Character.LINE_SEPARATOR || current == '\n' || current == ',' || current == ' ' || current == (char) (-1)) 
                            return Token.FPR.toString();
                        
                        else if(zeroToNine(current)){
                            current = readNextChar();
                            if(current != (char) (-1) && current != '\n' && current != ',' && current != ' ' && current != Character.LINE_SEPARATOR){
                                state = 21;
                                break;  
                            }
                            else
                                return Token.FPR.toString();
                        }
                        else{
                            state = 21;
                            break;
                        }    
                    }  
                }

                case 10:{
                    if(current == 'D'){
                        current = readNextChar();
                        if(current == 'A'){
                            state = 11;
                            break;
                        }
                        else if(current == 'M'){
                            state = 16;
                            break;
                        }
                        else{
                            state = 21;
                            break;
                        }
                    }
                }

                case 11:{
                    if(current == 'A'){
                        current = readNextChar();
                        state = 12;
                    }
                    else
                        state = 21;
                        break;
                }

                case 12:{
                    if(current == 'D'){
                        current = readNextChar();
                        state = 13;
                    }
                    else
                        state = 21;
                        break;
                }

                case 13:{
                    if(current == 'D'){
                        current = readNextChar();
                        if(current == 'I'){
                            state = 15;
                            break;
                        }
                        else{
                            state = 14;
                        }
                    }
                    else
                        state = 21;
                        break;              
                }

                case 14:{
                    if(current == 'U'){
                        current = readNextChar();
                        if(current != (char) (-1) && current != '\n' && current != ',' && current != ' ' && current != Character.LINE_SEPARATOR){
                            state = 21;
                        }
                        else
                            return Token.KEYWORD.toString(); 
                    }
                    else{
                        state = 21;
                        break;
                    }
                }
                
                case 15:{
                    current = readNextChar();
                    if(current == 'U'){
                        current = readNextChar();
                        if(current != (char) (-1) && current != '\n' && current != ',' && current != ' ' && current != Character.LINE_SEPARATOR){
                            state = 21;
                        }
                        else
                            return Token.KEYWORD.toString(); 
                    }
                    else
                        state = 21;
                        break;
                }
                
                case 16:{
                    if(current == 'M'){
                        current = readNextChar();
                        state = 17;
                    }
                    else
                        state = 21; 
                        break;
                }

                case 17:{
                    if(current == 'U'){
                        current = readNextChar();
                        state = 18;
                    }
                    else
                        state = 21;
                        break;                   
                }

                case 18:{
                    if(current == 'L'){
                        current = readNextChar();
                        state = 21;
                    }
                    else{
                        state = 21;
                        break;
                    }     
                }

                case 19:{
                    if(current == 'T'){
                        current = readNextChar();
                        if(current == 'U'){
                            state = 20;
                            break;
                        }
                        else{
                            if(current != (char) (-1) && current != '\n' && current != ',' && current != ' ' && current != Character.LINE_SEPARATOR){
                                state = 21;
                                break; 
                            }
    
                            else{
                                return Token.KEYWORD.toString();
                            }
                        }
                    }
                }

                case 20:{
                    if(current == 'U'){
                        current = readNextChar();
                        if(current != (char) (-1) && current != '\n' && current != ',' && current != ' ' && current != Character.LINE_SEPARATOR){
                            state = 21;
                        }
                        else
                            return Token.KEYWORD.toString(); 
                    }
                }

                case 21:{
                    if(current == '\n' || current == ',' && current == ' '){
                        while (current != (char) (-1) && current != '\n' && current != ',' && current != ' '){
                            current = readNextChar();
                        }
                        return Token.ERROR.toString();
                    }
                    else{
                        while (current != (char) (-1) && current != '\n' && current != ',' && current != ' '){
                            current = readNextChar();
                        }
                        return Token.ERROR.toString();
                    }
                }
                }
            }
    }
}