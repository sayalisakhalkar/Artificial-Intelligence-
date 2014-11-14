/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.*;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
/**
 *
 * @author Sayali
 */
public class agent {
    static boolean is_greedy = false;
    static boolean is_minimax = false;
    static boolean is_alpha_beta = false;
    static String data = "";
    static int counter = 0;
    static String method = "";
    static String player = "";
    static String cut_off_depth = "";                                        
    static String store_matrix[][] = new String[8][8];
    static String store_matrix_copy[][] = new String[8][8];
    static int evaluation_function[][] = new int[8][8];
    static boolean matrix_boolean[][] = new boolean[8][8];
    //static int cost_matrix[] = new int[64];
    static int evaluation_sum = 0;
    LinkedHashMap<String, String> h1 = new LinkedHashMap<String, String>();
    //int row_index[] = new int [64];
    //int col_index[] = new int [64]; 
    static PrintWriter out2;
    static String minimax_output1 = "";
    static String minimax_output = "";
    static String alpha_beta1 = "";
    static String max1 = "";
    static String max2 = "";
    static String max3 = "";
    public agent()
    {
    }
    public List compute_positions(String input[][], String curPlayer)
    {
        /* Arrays for storing row index and column index of possible positions ...Initialising them to 9 since the
           board is 8 X 8  
        */
        Set<Integer> positions = new HashSet<Integer>();
        String opponent = "O";
        if(curPlayer.equalsIgnoreCase("O")){
            opponent = "X";
        }
        int p = 0;
        int q = 0;
        //System.out.println("Computing positions...");
        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j < 8; j++)
            {
                if(input[i][j].equals(curPlayer))
                {
                    /* Traversing Left of 'player' */
                    p = i; q = j;
                    for(q = j-1; q >= 0 && input[i][q].equals(opponent); q--);
                    if(q >= 0 && input[i][q].equalsIgnoreCase("*") && input[i][q+1].equals(opponent)){
                           positions.add(Integer.parseInt(""+i+q));
                    }   
                    
                    /* Traversing Right of 'player' */
                    p = i; q = j;
                    for(q = j+1; q <8 && input[i][q].equals(opponent); q++);
                    if(q < 8 && input[i][q].equalsIgnoreCase("*") && input[i][q-1].equals(opponent)){
                           positions.add(Integer.parseInt(""+i+q));
                    }   
                        
                    /* Traversing Top of 'player' */    
                    p = i; q = j;
                    for(p = i-1; p >= 0  && input[p][j].equals(opponent); p--);
                    if(p>=0 && input[p][j].equalsIgnoreCase("*") && input[p+1][j].equals(opponent)){
                           positions.add(Integer.parseInt(""+p+j));
                    }    
                    
                    /* Traversing Bottom of 'player' */     
                    p = i; q = j;
                    for(p = i+1; p < 8   && input[p][j].equals(opponent); p++);
                    if(p < 8 && input[p][j].equalsIgnoreCase("*") && input[p-1][j].equals(opponent)){
                           positions.add(Integer.parseInt(""+p+j));
                    }    
                        
                    /* Traversing "top + left" of 'player' */     
                    p = i-1; q = j-1;
                    while(p >= 0 && q >=0 && input[p][q].equals(opponent)){
                        p--; q--;
                    }
                    if(p >= 0 && q >=0 && input[p][q].equalsIgnoreCase("*") && input[p+1][q+1].equals(opponent)){
                           positions.add(Integer.parseInt(""+p+q));
                    }       
                        
                    /* Traversing "top + right" of 'player' */          
                    p = i-1; q = j+1;
                    while(p >= 0 && q < 8 && input[p][q].equals(opponent)){
                        p--; q++;
                    }
                    if(p >= 0 && q < 8 && input[p][q].equalsIgnoreCase("*") && input[p+1][q-1].equals(opponent)){
                           positions.add(Integer.parseInt(""+p+q));
                    }    
                        
                    /* Traversing "Bottom + left" of 'player' */          
                    p = i+1; q = j-1;
                    while(p < 8 && q >= 0 && input[p][q].equals(opponent)){
                        p++; q--;
                    }
                    if(p < 8 && q >= 0 && input[p][q].equalsIgnoreCase("*") && input[p-1][q+1].equals(opponent)){
                           positions.add(Integer.parseInt(""+p+q));
                    }     
                        
                    /* Traversing "Bottom + right" of 'player' */   
                    p = i+1; q = j+1;
                    while(p < 8 && q < 8 && input[p][q].equals(opponent)){
                        p++; q++;
                    }
                    if(p < 8 && q < 8 && input[p][q].equalsIgnoreCase("*") && input[p-1][q-1].equals(opponent)){
                           positions.add(Integer.parseInt(""+p+q));
                    }         
                        
                }
            }
        }
        List<Integer> list = new ArrayList<Integer>(positions);
        Collections.sort(list);
        for(Integer i : list){
            //System.out.print(i+" ");
        }
        return list;
    }
    
    /* Calculating Evaluation Function Values for possible positions computed */
    public int compute_evaluation_function_values(String matrix[][], String curPlayer)
    {
        //displayMatrix(matrix);
        //System.out.println("In compute_evaluation_function_values");
        int sum_X = 0;
        int sum_O = 0;
        //int eval_index = 0;
        
        for(int m1 = 0; m1 < 8; m1++)
        {
            for(int m2 = 0; m2 < 8; m2++)
            {
                if(matrix[m1][m2].equalsIgnoreCase("X")){
                    sum_X += evaluation_function[m1][m2];
                } else if(matrix[m1][m2].equalsIgnoreCase("O")){
                    sum_O += evaluation_function[m1][m2];
                }
            }
        }
        //System.out.println("x and O = "+sum_X+" "+sum_O);
        if(curPlayer.equalsIgnoreCase("X")){
            return sum_X - sum_O;
        } else {
            return sum_O - sum_X;
        }
    }
    
    
    public String[][] generate_state(String curState[][], int x, int y, String curPlayer)
    {
            
            /* Co-ordinates after splitting */
            //System.out.println("x: "+x+"y: "+y);
            curState[x][y] = curPlayer;
            int j = 0;
            int i = 0 ;
            boolean found = false;
            
            //TODO if case fails, check farthest player and update all
            
            /* Traversing Up */
            j = y;
            for(i = (x)-1; i >= 0 && !curState[i][j].equalsIgnoreCase("*") ; i--)
            {
                if(curState[i][j].equalsIgnoreCase(curPlayer)){
                    found = true;
                    break;
                }
            }
            if(found){
                for(int k = x-1; k > i; k--){
                    curState[k][j] = curPlayer;
                }
            }
            
            
            /* Traversing Down */
            found = false;
            j = y;
            for(i = (x)+1; i < 8 && !curState[i][j].equalsIgnoreCase("*") ; i++)
            {
                if(curState[i][j].equalsIgnoreCase(curPlayer)){
                    found = true;
                    break;
                }
            }
            if(found){
                for(int k = x+1; k < i; k++){
                    curState[k][j] = curPlayer;
                }
            }
            
            /* Traversing right */
            found = false;
            i = x;
            for(j = (y)+1; j < 8 && !curState[i][j].equalsIgnoreCase("*") ; j++)
            {
                if(curState[i][j].equalsIgnoreCase(curPlayer)){
                    found = true;
                    break;
                }
            }
            if(found){
                for(int k = y+1; k < j; k++){
                    curState[i][k] = curPlayer;
                }
            }
            
            /* Traversing left */
            found = false;
            i = x;
            for(j = (y)-1; j >= 0 && !curState[i][j].equalsIgnoreCase("*") ; j--)
            {
                if(curState[i][j].equalsIgnoreCase(curPlayer)){
                    found = true;
                    break;
                }
            }
            if(found){
                for(int k = y-1; k > j; k--){
                    curState[i][k] = curPlayer;
                }
            }
            /* Traversing top-left */
            found = false;
            j = y-1;
            i = x-1;
            while(i >=0 && j>=0 && !curState[i][j].equalsIgnoreCase("*")){
                if(curState[i][j].equalsIgnoreCase(curPlayer)){
                    found = true;
                    break;
                }
                i--; j--;
            }
            if(found){
                int l = y-1;
                int k = x-1;
                while(k > i && l > j){
                    curState[k][l] = curPlayer;
                    k--; l--;
                }
            }
            
            /* Traversing top-right */
            found = false;
            j = y+1;
            i = x-1;
            while(i >=0 && j<8 && !curState[i][j].equalsIgnoreCase("*")){
                if(curState[i][j].equalsIgnoreCase(curPlayer)){
                    found = true;
                    break;
                }
                i--; j++;
            }
            if(found){
                int l = y+1;
                int k = x-1;
                while(k > i && l < j){
                    curState[k][l] = curPlayer;
                    k--; l++;
                }
            }
            
            /* Traversing bottom-left */
            found = false;
            j = y-1;
            i = x+1;
            while(i < 8 && j>=0 && !curState[i][j].equalsIgnoreCase("*")){
                if(curState[i][j].equalsIgnoreCase(curPlayer)){
                    found = true;
                    break;
                }
                i++; j--;
            }
            if(found){
                int l = y-1;
                int k = x+1;
                while(k < i && l > j){
                    curState[k][l] = curPlayer;
                    k++; l--;
                }
            }
            
            /* Traversing bottom-right */
            found = false;
            j = y+1;
            i = x+1;
            while(i < 8 && j < 8 && !curState[i][j].equalsIgnoreCase("*")){
                if(curState[i][j].equalsIgnoreCase(curPlayer)){
                    found = true;
                    break;
                }
                i++; j++;
            }
            if(found){
                int l = y+1;
                int k = x+1;
                while(k < i && l < j){
                    curState[k][l] = curPlayer;
                    k++; l++;
                }
            }   
       return curState;     
    }
        
    public void greedy()
    {
        //System.out.println("In Greedy function ..");
        String curState[][] = new String[8][8];
        String output[][] = new String[8][8];
        for(int i = 0; i < 8; i++)
        {
            System.arraycopy(store_matrix[i], 0, curState[i], 0, 8);
        }
        List<Integer> positions = compute_positions(curState, player);
        
        int max = Integer.MIN_VALUE;
        for(Integer pos : positions){
            String newGenerated[][] = new String[8][8];
            for(int i = 0; i < 8; i++)
            {
                System.arraycopy(curState[i], 0, newGenerated[i], 0, 8);
            }
            int x = pos / 10;
            int y = pos % 10;
            newGenerated = generate_state(newGenerated, x, y, player);
            int value = compute_evaluation_function_values(newGenerated, player);
            if(value > max){
                output = newGenerated;
                max = value;
            }
        }
        /* Next state printing */
        displayMatrix(output);
    }
    public void min_max(){
        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;
        String curState[][] = new String[8][8];
        String output[][] = new String[8][8];
        String curPlayer = player;
        int depth = 1;
        for(int i = 0; i < 8; i++)
        {
            System.arraycopy(store_matrix[i], 0, curState[i], 0, 8);
        }
        List<Integer> positions = compute_positions(curState, curPlayer);
        String opponant = "O";
        if(curPlayer.equalsIgnoreCase("O"))
            opponant = "X";
        boolean proceed = true;
        boolean isPass = false;
        if(positions == null || positions.isEmpty()){
            output = curState;
            proceed = false;
            positions = null;
            positions = compute_positions(curState, opponant);
            if(positions != null && !positions.isEmpty()){
                if(max == Integer.MAX_VALUE)
                {
                    max1 = "Infinity";
                    //System.out.println("root,0,"+max1);
                    minimax_output += "root,0,"+max1;
                }    
                else
                {    
                    if(max == Integer.MIN_VALUE)
                    {
                        max1 = "-Infinity";
                        //System.out.println("root,0,"+max1);
                        minimax_output += "root,0,"+max1;
                    }
                    else
                    {
                        //System.out.println("root,0,"+max);
                        minimax_output += "root,0,"+max;
                    }
                }    
                minimax_output += "\r\n";
                proceed = true;
                isPass = true;
                curPlayer = opponant;
                opponant = "O";
                if(curPlayer.equalsIgnoreCase("O"))
                    opponant = "X";
                depth++;
            }
            else 
            {
                int val = compute_evaluation_function_values(curState, curPlayer);
                if(val == Integer.MAX_VALUE)
                {
                    max1 = "Infinity";
                    //System.out.println("root,0,"+max1);
                    minimax_output += "root,0,"+max1;
                }    
                else
                {    
                    if(val == Integer.MIN_VALUE)
                    {
                        max1 = "-Infinity";
                        //System.out.println("root,0,"+max1);
                        minimax_output += "root,0,"+max1;
                    }
                    else
                    {
                        //System.out.println("root,0,"+val);
                        minimax_output += "root,0,"+val;
                    }
                }
                minimax_output += "\r\n";
            }
        }
        if(proceed)
        {
            for(Integer pos : positions){
                String newGenerated[][] = new String[8][8];
                for(int i = 0; i < 8; i++)
                {
                    System.arraycopy(curState[i], 0, newGenerated[i], 0, 8);
                }
                int x = pos / 10;
                int y = pos % 10;
                newGenerated = generate_state(newGenerated, x, y, curPlayer);
                
                if(!isPass){
                    if(max == Integer.MAX_VALUE)
                    {
                        max1 = "Infinity";
                        //System.out.println("root,0,"+max1);
                        minimax_output += "root,0,"+max1;
                    }    
                    else
                    {    
                        if(max == Integer.MIN_VALUE)
                        {
                            max1 = "-Infinity";
                            //System.out.println("root,0,"+max1);
                            minimax_output += "root,0,"+max1;
                        }
                        else
                        {
                            //System.out.println("root,0,"+max);
                            minimax_output += "root,0,"+max;
                        }
                    }    
                    minimax_output += "\r\n";
                    int value = get_Min_Max(newGenerated, depth, opponant, x, y);
                        if(value > max){
                        output = newGenerated;
                        max = value;
                    }
                } else {
                    if(min == Integer.MAX_VALUE)
                    {
                        max1 = "Infinity";
                        //System.out.println("pass,1,"+max1);
                        minimax_output += "pass,1,"+max1;
                    }    
                    else
                    {    
                        if(min == Integer.MIN_VALUE)
                        {
                            max1 = "-Infinity";
                            //System.out.println("pass,1,"+max1);
                            minimax_output += "pass,1,"+max1;
                        }
                        else
                        {
                            //System.out.println("pass,1,"+min);
                            minimax_output += "pass,1,"+min;
                        }
                    }    
                    minimax_output += "\r\n";
                    int value = get_Min_Max(newGenerated, depth, opponant, x, y);
                        if(value < min){
                        //output = newGenerated;
                        min = value;
                    }
                }
            }
            if(isPass)
            {
                if(min == Integer.MAX_VALUE)
                {    
                    max1 = "Infinity";
                    //System.out.println("pass,1,"+max1);
                    minimax_output += "pass,1,"+max1;
                }    
                else
                {    
                    if(min == Integer.MIN_VALUE)
                    {
                        max1 = "-Infinity";
                        //System.out.println("pass,1,"+max1);
                        minimax_output += "pass,1,"+max1;
                    }
                    else
                    {
                        //System.out.println("pass,1,"+min);
                        minimax_output += "pass,1,"+min;
                    }
                }    
                minimax_output += "\r\n";
                max = min;
                if(max == Integer.MAX_VALUE)
                {    
                    max1 = "Infinity";
                    //System.out.println("root,0,"+max1);
                    minimax_output += "root,0,"+max1;
                }    
                else
                {    
                    if(max == Integer.MIN_VALUE)
                    {
                        max1 = "-Infinity";
                        //System.out.println("root,0,"+max1);
                        minimax_output += "root,0,"+max1;
                    }
                    else
                    {
                        //System.out.println("root,0,"+max);
                        minimax_output += "root,0,"+max;
                    }
                }    
                minimax_output += "\r\n";
            } 
            else 
            {
                if(max == Integer.MAX_VALUE)
                {    
                    max1 = "Infinity";
                    //System.out.println("root,0,"+max1);
                    minimax_output += "root,0,"+max1;
                }    
                else
                {    
                    if(max == Integer.MIN_VALUE)
                    {
                        max1 = "-Infinity";
                        //System.out.println("root,0,"+max1);
                        minimax_output += "root,0,"+max1;
                    }
                    else
                    {
                        //System.out.println("root,0,"+max);
                        minimax_output += "root,0,"+max;
                    }
                }    
                minimax_output += "\r\n";
            }
        }
        /* Next state printing */
        displayMatrix(output);
    }
    
    public int get_Min_Max(String state[][], int depth_val, String player1, int x1, int y1){
        //displayMatrix(state);
        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;
        String prefixx = "";
        prefixx = (x1 == -1 ? "pass" : ((char)(y1+1+96)+""+(x1+1)));
        if(Integer.parseInt(cut_off_depth) == depth_val){
            int val = compute_evaluation_function_values(state, player);
            if(val == Integer.MAX_VALUE)
            {
                max1 = "Infinity";
                //System.out.println(prefixx+","+depth_val+","+max1);
                minimax_output += prefixx+","+depth_val+","+max1;
            }    
            else
            {    
                if(val == Integer.MIN_VALUE)
                {
                    max1 = "-Infinity";
                    //System.out.println(prefixx+","+depth_val+","+max1);
                    minimax_output += prefixx+","+depth_val+","+max1;
                }
                else
                {
                    //System.out.println(prefixx+","+depth_val+","+val);
                    minimax_output += prefixx+","+depth_val+","+val;
                }
            }    
            minimax_output += "\r\n";
            return val;
        }
        
        String curState[][] = new String[8][8];
        for(int i = 0; i < 8; i++)
        {
            System.arraycopy(state[i], 0, curState[i], 0, 8);
        }
        //int v = Integer.MIN_VALUE;
        List<Integer> positions = compute_positions(curState, player1);
        String opponant = "O";
        if(player1.equalsIgnoreCase("O"))
            opponant = "X";
        if(positions == null || positions.isEmpty()){
            positions = null;
            positions = compute_positions(curState, opponant);
            if(positions != null && !positions.isEmpty()){
                if(min == Integer.MAX_VALUE)
                {    
                    max1 = "Infinity";
                    //System.out.println(prefixx+","+depth_val+","+max1);
                    minimax_output += prefixx+","+depth_val+","+max1;
                }    
                else
                {    
                    if(min == Integer.MIN_VALUE)
                    {
                        max1 = "-Infinity";
                        //System.out.println(prefixx+","+depth_val+","+max1);
                        minimax_output += prefixx+","+depth_val+","+max1;
                    }
                    else
                    {
                        //System.out.println(prefixx+","+depth_val+","+min);
                        minimax_output += prefixx+","+depth_val+","+min;
                    }
                }     
                minimax_output += "\r\n";
                int temp = get_Min_Max(curState, depth_val+1, opponant, -1, -1);
                if(temp < min) min = temp;
                if(min == Integer.MAX_VALUE)
                {
                    max1 = "Infinity";
                    //System.out.println(prefixx+","+depth_val+","+max1);
                    minimax_output += prefixx+","+depth_val+","+max1;
                }    
                else
                {    
                    if(min == Integer.MIN_VALUE)
                    {
                        max1 = "-Infinity";
                        //System.out.println(prefixx+","+depth_val+","+max1);
                        minimax_output += prefixx+","+depth_val+","+max1;
                    }
                    else
                    {
                        //System.out.println(prefixx+","+depth_val+","+min);
                        minimax_output += prefixx+","+depth_val+","+min;
                    }
                }
                minimax_output += "\r\n";
                return min;
                
            } else {
                int val = compute_evaluation_function_values(state, player);
                if(val == Integer.MAX_VALUE)
                {    
                    max1 = "Infinity";
                    //System.out.println(prefixx+","+depth_val+","+max1);
                    minimax_output += prefixx+","+depth_val+","+max1;
                }    
                else
                {
                    if(val == Integer.MIN_VALUE)
                    {
                        max1 = "-Infinity";
                        //System.out.println(prefixx+","+depth_val+","+max1);
                        minimax_output += prefixx+","+depth_val+","+max1;
                    }
                    else
                    {
                        //System.out.println(prefixx+","+depth_val+","+val);
                        minimax_output += prefixx+","+depth_val+","+val;
                    }
                }    
                minimax_output += "\r\n";
                return val;
            }
          
        }
        opponant = "O";
        if(player1.equalsIgnoreCase("O"))
            opponant = "X";
        
        if(player1.equalsIgnoreCase(player)){

            for(Integer pos : positions){
                String newGenerated[][] = new String[8][8];
                for(int i = 0; i < 8; i++)
                {
                    System.arraycopy(curState[i], 0, newGenerated[i], 0, 8);
                }
                int x = pos / 10;
                int y = pos % 10;
                newGenerated = generate_state(newGenerated, x, y, player1);
                prefixx = (x1 == -1 ? "pass" : ((char)(y1+1+96)+""+(x1+1)));
                if(max == Integer.MAX_VALUE)
                {    
                    max1 = "Infinity";
                    //System.out.println(prefixx+","+depth_val+","+max1);
                    minimax_output += prefixx+","+depth_val+","+max1;
                }    
                else
                {
                    if(max == Integer.MIN_VALUE)
                    {
                        max1 = "-Infinity";
                        //System.out.println(prefixx+","+depth_val+","+max1);
                        minimax_output += prefixx+","+depth_val+","+max1;
                    }
                    else
                    {
                        //System.out.println(prefixx+","+depth_val+","+max);
                        minimax_output += prefixx+","+depth_val+","+max;
                    }
                }    
                minimax_output += "\r\n";
                int value = get_Min_Max(newGenerated, depth_val+1, opponant, x, y);
                if(value > max){
                    max = value;
                }
            }
            prefixx = (x1 == -1 ? "pass" : ((char)(y1+1+96)+""+(x1+1)));
            if(max == Integer.MAX_VALUE)
            {
                max1 = "Infinity";
                //System.out.println(prefixx+","+depth_val+","+max1);
                minimax_output += prefixx+","+depth_val+","+max1;
            }    
            else
            {
                if(max == Integer.MIN_VALUE)
                {
                    max1 = "-Infinity";
                    //System.out.println(prefixx+","+depth_val+","+max1);
                    minimax_output += prefixx+","+depth_val+","+max1;
                }
                else
                {
                    //System.out.println(prefixx+","+depth_val+","+max);
                    minimax_output += prefixx+","+depth_val+","+max;
                }    
            }    
            minimax_output += "\r\n";
            return max;
        } else {
            for(Integer pos : positions){
                String newGenerated[][] = new String[8][8];
                for(int i = 0; i < 8; i++)
                {
                    System.arraycopy(curState[i], 0, newGenerated[i], 0, 8);
                }
                int x = pos / 10;
                int y = pos % 10;
                newGenerated = generate_state(newGenerated, x, y, player1);
                prefixx = (x1 == -1 ? "pass" : ((char)(y1+1+96)+""+(x1+1)));
                if(min == Integer.MAX_VALUE)
                {
                    max1 = "Infinity";
                    //System.out.println(prefixx+","+depth_val+","+max1);
                    minimax_output += prefixx+","+depth_val+","+max1;
                }
                else
                {
                    if(min == Integer.MIN_VALUE)
                    {
                        max1 = "-Infinity";
                        //System.out.println(prefixx+","+depth_val+","+max1);
                        minimax_output += prefixx+","+depth_val+","+max1;
                    }
                    else
                    {
                        //System.out.println(prefixx+","+depth_val+","+min);
                        minimax_output += prefixx+","+depth_val+","+min;
                    }
                }
                minimax_output += "\r\n";
                int value = get_Min_Max(newGenerated, depth_val+1, opponant, x, y);
                if(value < min){
                    min = value;
                }
            }
            prefixx = (x1 == -1 ? "pass" : ((char)(y1+1+96)+""+(x1+1)));
            if(min == Integer.MAX_VALUE)
            {
                max1 = "Infinity";
                //System.out.println(prefixx+","+depth_val+","+max1);
                minimax_output += prefixx+","+depth_val+","+max1;
            }
            else
            {
                if(min == Integer.MIN_VALUE)
                {
                    max1 = "-Infinity";
                    //System.out.println(prefixx+","+depth_val+","+max1);
                    minimax_output += prefixx+","+depth_val+","+max1;
                }
                else
                {
                    //System.out.println(prefixx+","+depth_val+","+min);
                    minimax_output += prefixx+","+depth_val+","+min;
                }    
            }    
            minimax_output += "\r\n";
            return min;
        }
    }
    
    public void alpha_beta()
    {
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        
        int alphaP = Integer.MIN_VALUE;
        int betaP = Integer.MAX_VALUE;
        String curState[][] = new String[8][8];
        String output[][] = new String[8][8];
        String curPlayer = player;
        int depth = 1;
        for(int i = 0; i < 8; i++)
        {
            System.arraycopy(store_matrix[i], 0, curState[i], 0, 8);
        }
        List<Integer> positions = compute_positions(curState, curPlayer);
        String opponant = "O";
        if(curPlayer.equalsIgnoreCase("O"))
            opponant = "X";
        boolean proceed = true;
        boolean isPass = false;
        if(positions == null || positions.isEmpty()){
            output = curState;
            proceed = false;
            positions = null;
            positions = compute_positions(curState, opponant);
            if(positions != null && !positions.isEmpty()){
                //System.out.println("root,0,"+alpha+","+alpha+","+beta);
                alpha_beta1 += "root,0,"+alpha+","+alpha+","+beta;
                alpha_beta1 += "\r\n";
                proceed = true;
                isPass = true;
                curPlayer = opponant;
                opponant = "O";
                if(curPlayer.equalsIgnoreCase("O"))
                    opponant = "X";
                depth++;
            } else {
                int val = compute_evaluation_function_values(curState, curPlayer);
                //System.out.println("root,0,"+val+","+alpha+","+beta);
                alpha_beta1 += "root,0,"+val+","+alpha+","+beta;
                alpha_beta1 += "\r\n";
            }
                
        }
        if(proceed){
            for(Integer pos : positions){
            String newGenerated[][] = new String[8][8];
                for(int i = 0; i < 8; i++)
                {
                    System.arraycopy(curState[i], 0, newGenerated[i], 0, 8);
                }
                int x = pos / 10;
                int y = pos % 10;
                newGenerated = generate_state(newGenerated, x, y, curPlayer);
                if(!isPass){
                    //System.out.println("root,0,"+alpha+","+alpha+","+beta);
                    alpha_beta1 += "root,0,"+alpha+","+alpha+","+beta;
                    alpha_beta1 += "\r\n";
                    int newAlpha = minValue(newGenerated, depth, opponant, x, y, alpha, beta);
            
                    if(newAlpha > alpha){
                        alpha = newAlpha;
                        output = newGenerated;
                    }
                } else {
                    //System.out.println("pass,1,"+betaP+","+alphaP+","+betaP);
                    alpha_beta1 += "pass,1,"+betaP+","+alphaP+", "+betaP;
                    alpha_beta1 += "\r\n";
                    int newBeta = maxValue(newGenerated, depth, opponant, x, y, alphaP, betaP);
            
                    if(newBeta < betaP){
                        betaP = newBeta;
                        //output = newGenerated;
                    }
                }
                
        }
            if(isPass){
                //System.out.println("pass,1,"+betaP+","+alphaP+","+betaP);
                    alpha_beta1 += "pass, 1,"+betaP+","+alphaP+","+betaP;
                    alpha_beta1 += "\r\n";
                alpha = betaP;    
                //System.out.println("root,0,"+alpha+","+alpha+","+beta);
                alpha_beta1 += "root,0,"+alpha+","+alpha+","+beta;
                alpha_beta1 += "\r\n";
            } else {
                //System.out.println("root,0,"+alpha+","+alpha+","+beta);
                alpha_beta1 += "root,0,"+alpha+","+alpha+","+beta;
                alpha_beta1 += "\r\n";
            }
        }        
        displayMatrix(output);
        //maxValue(curState, 0, player, -1,-1, alpha, beta);
    }
    
     public int maxValue(String state[][], int depth_val, String player1, int x1, int y1, int alpha, int beta){
        // Cut off state
        String prefix = (x1 == -1 ? "pass" : ((char)(y1+1+96)+""+(x1+1)));
        if(Integer.parseInt(cut_off_depth) == depth_val){
            int val = compute_evaluation_function_values(state, player);
            //System.out.println(prefix+","+depth_val+","+val+","+alpha+","+beta);
            alpha_beta1 += prefix+","+depth_val+","+val+","+alpha+","+beta;
            alpha_beta1 += "\r\n";
            return val;
        }
        // Get successor
        String curState[][] = new String[8][8];
        for(int i = 0; i < 8; i++)
        {
            System.arraycopy(state[i], 0, curState[i], 0, 8);
        }
        int v = Integer.MIN_VALUE;
        List<Integer> positions = compute_positions(curState, player1);
        String opponant = "O";
        if(player1.equalsIgnoreCase("O"))
            opponant = "X";
        if(positions == null || positions.isEmpty()){
            
            positions = null;
            positions = compute_positions(curState, opponant);
            if(positions != null && !positions.isEmpty()){
                //System.out.println(prefix+","+depth_val+","+alpha+","+alpha+","+beta);
                alpha_beta1 += prefix+","+depth_val+","+alpha+","+alpha+","+beta;
                alpha_beta1 += "\r\n";
                int temp = maxValue(curState, depth_val+1, opponant, -1, -1, alpha, beta);
                if(temp > alpha) alpha = temp;
                //System.out.println(prefix+","+depth_val+","+alpha+","+alpha+","+beta);
                alpha_beta1 += prefix+","+depth_val+","+alpha+","+alpha+","+beta;
                alpha_beta1 += "\r\n";
                return alpha;
                
            } else {
                int val = compute_evaluation_function_values(state, player);
                //System.out.println(prefix+","+depth_val+","+val+","+alpha+","+beta);
                alpha_beta1 += prefix+","+depth_val+","+val+","+alpha+","+beta;
                alpha_beta1 += "\r\n";
                return val;
            }
            
        }
        
        for(Integer pos : positions){
            String newGenerated[][] = new String[8][8];
                for(int i = 0; i < 8; i++)
                {
                    System.arraycopy(curState[i], 0, newGenerated[i], 0, 8);
                }
                int x = pos / 10;
                int y = pos % 10;
                newGenerated = generate_state(newGenerated, x, y, player1);
                
            //System.out.println(prefix+","+depth_val+","+v+","+alpha+","+beta);
            alpha_beta1 += prefix+","+depth_val+","+v+","+alpha+","+beta;
            alpha_beta1 += "\r\n";
            int newAlpha = minValue(newGenerated, depth_val+1, opponant, x, y, alpha, beta);
            if(newAlpha > v)
                v = newAlpha;
            if(v >= beta){
                if(v > alpha)
                    alpha = v;
                //System.out.println(prefix+","+depth_val+","+alpha+","+alpha+","+beta);
                alpha_beta1 += prefix+","+depth_val+","+alpha+","+alpha+","+beta;
                alpha_beta1 += "\r\n";
                return v;
            }
            if(v > alpha)
                alpha = v;
        }
        //System.out.println(prefix+","+depth_val+","+v+","+alpha+","+beta);
        alpha_beta1 += prefix+","+depth_val+","+v+","+alpha+","+beta;
        alpha_beta1 += "\r\n";
        return v;
    }
    
    public int minValue(String state[][], int depth_val, String player1, int x1, int y1, int alpha, int beta){
        // Cut off state
        // Cut off state
        String prefix = (x1 == -1 ? "pass" : ((char)(y1+1+96)+""+(x1+1)));
        if(Integer.parseInt(cut_off_depth) == depth_val){
            int val = compute_evaluation_function_values(state, player);
            //System.out.println(prefix+","+depth_val+","+val+","+alpha+","+beta);
            alpha_beta1 += prefix+","+depth_val+","+val+","+alpha+","+beta;
            alpha_beta1 += "\r\n";
            return val;
        }
        // Get successor
        String curState[][] = new String[8][8];
        for(int i = 0; i < 8; i++)
        {
            System.arraycopy(state[i], 0, curState[i], 0, 8);
        }
        int v = Integer.MAX_VALUE;
        List<Integer> positions = compute_positions(curState, player1);
        String opponant = "O";
        if(player1.equalsIgnoreCase("O"))
            opponant = "X";
        if(positions == null || positions.isEmpty()){
            
            positions = null;
            positions = compute_positions(curState, opponant);
            if(positions != null && !positions.isEmpty()){
                //System.out.println(prefix+","+depth_val+","+beta+","+alpha+","+beta);
                alpha_beta1 += prefix+","+depth_val+","+beta+","+alpha+","+beta;
                alpha_beta1 += "\r\n";
                int temp = 0;
                temp = maxValue(curState, depth_val+1, opponant, -1, -1, alpha, beta);
                if(temp < beta) beta = temp;
                //System.out.println(prefix+","+depth_val+","+beta+","+alpha+","+beta);
                alpha_beta1 += prefix+","+depth_val+","+beta+","+alpha+","+beta;
                alpha_beta1 += "\r\n";
                return beta;
                
            } else {
                int val = compute_evaluation_function_values(state, player);
                //System.out.println(prefix+","+depth_val+","+val+","+alpha+","+beta);
                alpha_beta1 += prefix+","+depth_val+","+val+","+alpha+","+beta;
                alpha_beta1 += "\r\n";
                return val;
            }
        }
        for(Integer pos : positions){
            String newGenerated[][] = new String[8][8];
                for(int i = 0; i < 8; i++)
                {
                    System.arraycopy(curState[i], 0, newGenerated[i], 0, 8);
                }
                int x = pos / 10;
                int y = pos % 10;
                newGenerated = generate_state(newGenerated, x, y, player1);
            //System.out.println(prefix+","+depth_val+","+v+","+alpha+","+beta);
            alpha_beta1 += prefix+","+depth_val+","+v+","+alpha+","+beta;
            alpha_beta1 += "\r\n";
            int newBeta = maxValue(newGenerated, depth_val+1, opponant, x, y, alpha, beta);
            
            if(newBeta < v)
                v = newBeta;
            if(v <= alpha){
                if(v < beta)
                    beta = v;
                //System.out.println(prefix+","+depth_val+","+beta+","+alpha+","+beta);
                alpha_beta1 += prefix+","+depth_val+","+beta+","+alpha+","+beta;
                alpha_beta1 += "\r\n";
                return v;
            }
            if(v < beta)
                beta = v;
        }
        //System.out.println(prefix+","+depth_val+","+v+","+alpha+","+beta);
            alpha_beta1 += prefix+","+depth_val+","+v+","+alpha+","+beta;
            alpha_beta1 += "\r\n";
        return v;
    }
    
    public void construct_evaluation_matrix()
    {
        //System.out.println("Constructing Evaluation matrix..");
        evaluation_function[0][0] = 99;
        evaluation_function[0][7] = 99;
        evaluation_function[7][0] = 99;
        evaluation_function[7][7] = 99;
        //System.out.println("Evaluation matrix: ");
        //System.out.println("Evaluation function hard-coded values");
        evaluation_function[0][0] = 99;
        evaluation_function[0][1] = -8;
        evaluation_function[0][2] = 8;
        evaluation_function[0][3] = 6;
        evaluation_function[0][4] = 6;
        evaluation_function[0][5] = 8;
        evaluation_function[0][6] = -8;
        evaluation_function[0][7] = 99;
        evaluation_function[1][0] = -8;
        evaluation_function[1][1] = -24;
        evaluation_function[1][2] = -4;
        evaluation_function[1][3] = -3;
        evaluation_function[1][4] = -3;
        evaluation_function[1][5] = -4;
        evaluation_function[1][6] = -24;
        evaluation_function[1][7] = -8;
        evaluation_function[2][0] = 8;
        evaluation_function[2][1] = -4;
        evaluation_function[2][2] = 7;
        evaluation_function[2][3] = 4;
        evaluation_function[2][4] = 4;
        evaluation_function[2][5] = 7;
        evaluation_function[2][6] = -4;
        evaluation_function[2][7] = 8;
        evaluation_function[3][0] = 6;
        evaluation_function[3][1] = -3;
        evaluation_function[3][2] = 4;
        evaluation_function[3][3] = 0;
        evaluation_function[3][4] = 0;
        evaluation_function[3][5] = 4;
        evaluation_function[3][6] = -3;
        evaluation_function[3][7] = 6;
        evaluation_function[4][0] = 6;
        evaluation_function[4][1] = -3;
        evaluation_function[4][2] = 4;
        evaluation_function[4][3] = 0;
        evaluation_function[4][4] = 0;
        evaluation_function[4][5] = 4;
        evaluation_function[4][6] = -3;
        evaluation_function[4][7] = 6;
        evaluation_function[5][0] = 8;
        evaluation_function[5][1] = -4;
        evaluation_function[5][2] = 7;
        evaluation_function[5][3] = 4;
        evaluation_function[5][4] = 4;
        evaluation_function[5][5] = 7;
        evaluation_function[5][6] = -4;
        evaluation_function[5][7] = 8;
        evaluation_function[6][0] = -8;
        evaluation_function[6][1] = -24;
        evaluation_function[6][2] = -4;
        evaluation_function[6][3] = -3;
        evaluation_function[6][4] = -3;
        evaluation_function[6][5] = -4;
        evaluation_function[6][6] = -24;
        evaluation_function[6][7] = -8;
        evaluation_function[7][0] = 99;
        evaluation_function[7][1] = -8;
        evaluation_function[7][2] = 8;
        evaluation_function[7][3] = 6;
        evaluation_function[7][4] = 6;
        evaluation_function[7][5] = 8;
        evaluation_function[7][6] = -8;
        evaluation_function[7][7] = 99;
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception
    {
        // TODO code application logic here
            try
            {
                out2 = new PrintWriter(new FileWriter("output.txt"));   
            }    
            catch(Exception e)
            {

            }
            int matrix_row = 0;
            counter = 0;
            method = "";
            player = "";
            cut_off_depth = "";
            File f1 = new File("input.txt");
            FileReader fr = new FileReader(f1);
            BufferedReader br = new BufferedReader(fr);
            data = br.readLine();
            while(data != null)
            {
                if (counter == 0)
                {
                    if(data.equals("1"))
                    {
                        method = "greedy";
                        //System.out.println("method: "+method);
                    }
                    if(data.equals("2"))
                    {
                        method = "MiniMax";
                        //System.out.println("method: "+method);
                    }  
                    if(data.equals("3"))
                    {
                        method = "alpha_beta";
                        //System.out.println("method: "+method);
                    }
                }
                if(counter == 1)
                {
                    player = data;
                    //System.out.println("Player is :");
                    //System.out.println(" "+player);
                }
                if(counter == 2)
                {
                    cut_off_depth = data;
                    //System.out.println("Cut-off Depth is :");
                    //System.out.println(" "+cut_off_depth);
                }
                if(counter >= 3 && counter < (4 + 8 + 8))
                {
                    for(int i1 = 0; i1< 8; i1++)
                    {
                        store_matrix[matrix_row][i1] = (data.charAt(i1)+"");
                    }
                    //System.out.println("matrix_row: "+matrix_row+"data: "+data);
                    matrix_row = matrix_row + 1;
                }
                data = br.readLine();
                counter = counter + 1;
            }
            if(method.equals("greedy"))
            {
                is_greedy = true;
                agent p = new agent();
                p.construct_evaluation_matrix();
                p.greedy();
            }
            if(method.equals("MiniMax"))
            {
                is_minimax = true;
                String curState[][] = new String[8][8];
                agent p = new agent();
                for(int i = 0; i < 8; i++)
                {
                    for(int j = 0; j < 8; j++)
                    {
                        curState[i][j] = store_matrix[i][j];
                    }
                }
                p.construct_evaluation_matrix();
                p.min_max();
                //System.out.println("minimax_output: "+minimax_output);
                out2.println(minimax_output);
                out2.close();
            }
            if(method.equals("alpha_beta"))
            {
                is_alpha_beta = true;
                agent p = new agent();
                p.construct_evaluation_matrix();
                p.alpha_beta();
                //System.out.println("alpha-beta output: "+alpha_beta1);
                int max_int_val = Integer.MAX_VALUE;
                int min_int_val = Integer.MIN_VALUE;
                //System.out.println("max: "+max_int_val+"min: "+min_int_val);
                String max_int = max_int_val+"";
                String min_int = min_int_val+"";
                alpha_beta1 = alpha_beta1.replaceAll(max_int, "Infinity");
                alpha_beta1 = alpha_beta1.replaceAll(min_int, "-Infinity");
                out2.println(alpha_beta1);
                out2.close();
            }
    }

    private void displayMatrix(String[][] output) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                //System.out.print(output[i][j]);
                if(is_minimax == true)
                {
                    minimax_output1 += output[i][j];
                    out2.print(output[i][j]);
                } 
                if(is_greedy == true)
                {
                    out2.print(output[i][j]);
                }
                if(is_alpha_beta == true)
                {
                    out2.print(output[i][j]);
                }
            }
            if(is_alpha_beta == true)
            {
                out2.println();
            }
            if(is_minimax == true)
            {
                minimax_output1 += "\n";
                out2.println();
            }
            if(is_greedy == true)
            {
                out2.println();
            }
            //System.out.println();
        }
        if(is_minimax)
        {
            out2.println("Node,Depth,Value");
        }    
        if(is_greedy)
        {
            out2.close();
        }
        if(is_alpha_beta)
        {
            out2.println("Node,Depth,Value,Alpha,Beta");
        }
    }
}
