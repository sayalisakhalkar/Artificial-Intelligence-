/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary;
import java.io.*;
import java.util.*;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Sayali
 */
public class agent 
{
    public static HashMap<String,Integer> map = new HashMap<String,Integer>();
    public static List<Map.Entry<String, Integer>> map2 = new ArrayList<Map.Entry<String, Integer>>();
    public static int ucs_status = 0;
    public static String removed_element = "";
    static String bfs_path = "";
    static int index = 0;
    static String source = "";
    static String destination = "";
    static int total_nodes = 0;
    //boolean a[][];
    static boolean visited[];
    static String nodes[] = new String[total_nodes];
    static String store_list[] = new String[total_nodes];  
    static String store_matrix[][] = new String[total_nodes][total_nodes];
    static boolean status = false;
    public agent()
    {
        //a = new boolean[total_nodes][total_nodes];
    }  
    public class Node2 implements Comparable<Node2>{
        String name;
        Node2 parent;

        public Node2(String name, Node2 parent) {
            this.name = name;
            this.parent = parent;
        }

        @Override
        public boolean equals(Object obj) {
            if(!(obj instanceof Node1)){
                return false;
            }
            if(this.name.equalsIgnoreCase(((Node1)obj).name)){
                return true;
            }
                
            return false; //To change body of generated methods, choose Tools | Templates.
        }
       
        public int compareTo(Node2 o) {
                return (this.name.compareTo(o.name) * -1);
        }
        
    }
    
    public void dfs1(){
        Node2 root = new Node2(source, null);
        String log = "";
        boolean visited1[] = new boolean[total_nodes];
        Stack stack = new Stack();
        stack.push(root);
        visited1[Arrays.asList(store_list).indexOf(source)] = true;
        Node2 temp;
        while(true){
            try {
                PrintWriter out1 = new PrintWriter(new FileWriter("output.txt"));
                String path1 = "";     
                String path = "";
                if(stack.empty()){
                    log = log.substring(0, log.length() - 1);
					//System.out.println(log);
                    out1.println(log);
                    path += "No path available";
					//System.out.println(path);
                    out1.println(path);
                    out1.close();
                    return;
                }
                temp = (Node2)stack.pop();
                log += temp.name+"-";
                if(temp.name.equalsIgnoreCase(destination)){
                    // TODO print output and cost
                    log = log.substring(0, log.length() - 1);
					//System.out.println(log);
                    out1.println(log);
                    int cost = 0;
                    //System.out.println("cost: "+temp.weight);
                    while(temp != null){
                        if(temp.parent != null){
                            int i = Arrays.asList(store_list).indexOf(temp.parent.name);
                            int j = Arrays.asList(store_list).indexOf(temp.name);
                            cost += Integer.parseInt(store_matrix[i][j]);
                        }
                        path = temp.name+"-"+path;
                        temp = temp.parent;
                    }
                    path = path1 + path;
                    path = path.substring(0, path.length() - 1);
					//System.out.println(path);
                    out1.println(path);
					//System.out.println(cost);
                    out1.println(cost);
                    out1.close();
                    return;
                }
                int indexOfTemp = Arrays.asList(store_list).indexOf(temp.name);
                List<Node2> tempList = new ArrayList<Node2>();
                for(int i = 0; i < total_nodes; i++){
                    if(!visited1[i] && (Integer.parseInt(store_matrix[indexOfTemp][i]) > 0)){// If neighbour is not explored then visit
                        Node2 newNode = new Node2(store_list[i], temp);
                        tempList.add(newNode);
                    }
                }
                Collections.sort(tempList);
                for(Node2 n2 : tempList){
                    stack.push(n2);
                    visited1[Arrays.asList(store_list).indexOf(n2.name)] = true; 
                }
            } catch (Exception ex) {
                Logger.getLogger(agent.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
    public void bfs1(){
        Node1 root = new Node1(source, 0, null);
        String log = "";
        //double cost = 0;
        boolean visited1[] = new boolean[total_nodes];
        PriorityQueue<Node1> queue = new PriorityQueue<Node1>();
        queue.add(root);
        Node1 temp;
        while(true){
            try
                {
                PrintWriter out2 = new PrintWriter(new FileWriter("output.txt"));    
                String path1 = "";
                String path = "";
                if(queue.isEmpty()){
                    log = log.substring(0, log.length() - 1);
					//System.out.println(log);
                    out2.println(log);
                    path += "No path available";
					//System.out.println(path);
                    out2.println(path);
                    out2.close();
                    return;
                }
                temp = queue.poll();
                log += temp.name+"-";
                if(temp.name.equalsIgnoreCase(destination)){
                    // TODO print output and cost
                    log = log.substring(0, log.length() - 1);
					//System.out.println(log);
                    out2.println(log);
                    int cost = 0;
                    //System.out.println("cost: "+temp.weight);
                    while(temp != null){
                        if(temp.parent != null){
                            int i = Arrays.asList(store_list).indexOf(temp.parent.name);
                            int j = Arrays.asList(store_list).indexOf(temp.name);
                            cost += Integer.parseInt(store_matrix[i][j]);
                        }
                        path = temp.name+"-"+path;
                        temp = temp.parent;
                    }
                    path = path1 + path;
                    path = path.substring(0, path.length() - 1);
					//System.out.println(path);
                    out2.println(path);
					//System.out.println(cost);
                    out2.println(cost);
                    out2.close();
                    return;
                }
                int indexOfTemp = Arrays.asList(store_list).indexOf(temp.name);
                visited1[indexOfTemp] = true;
                for(int i = 0; i < total_nodes; i++){
                    if(!visited1[i] && (Integer.parseInt(store_matrix[indexOfTemp][i]) > 0)){// If neighbour is not explored then visit
                        Node1 newNode = new Node1(store_list[i], (temp.weight + 1), temp);
                       if(queue.contains(newNode)){
                           Iterator itr = queue.iterator();
                           while(itr.hasNext()) {
                               Node1 element = (Node1) itr.next();
                               if(element.equals(newNode) && element.weight > newNode.weight){
                                   queue.remove(element);
                                   queue.add(newNode);
                                   break;
                               }
                           }
                       } else {
                           queue.add(newNode);
                       }
                    }
                }
            }
            catch(Exception e)
            {
                System.out.println(e);
            }
        }
    }
    
    public class Node1 implements Comparable<Node1>{
        String name;
        int weight;
        Node1 parent;

        public Node1(String name, int weight, Node1 parent) {
            this.name = name;
            this.weight = weight;
            this.parent = parent;
        }

        @Override
        public boolean equals(Object obj) {
            if(!(obj instanceof Node1)){
                return false;
            }
            if(this.name.equalsIgnoreCase(((Node1)obj).name)){
                return true;
            }
                
            return false; //To change body of generated methods, choose Tools | Templates.
        }
       
        public int compareTo(Node1 o) {
            if(this.weight > o.weight)
                return 1;
            else if(this.weight< o.weight)
                return -1;
            else{
                return this.name.compareTo(o.name);
            }
        }
        
    }
    
    public void ucs1(){
        Node1 root = new Node1(source, 0, null);
        String log = "";
        int cost = 0;
        boolean visited1[] = new boolean[total_nodes];
        PriorityQueue<Node1> queue = new PriorityQueue<Node1>();
        queue.add(root);
        Node1 temp;
        while(true){
            try
            {
                PrintWriter out = new PrintWriter(new FileWriter("output.txt"));
                String path1 = "";
                String path = "";
                if(queue.isEmpty()){
                    log = log.substring(0, log.length() - 1);
					//System.out.println(log);
                    out.println(log);
                    path += "No path available";
					//System.out.println(path);
                    out.println(path);
                    out.close();
                    return;
                }
                temp = queue.poll();
                log += temp.name+"-";
                if(temp.name.equalsIgnoreCase(destination)){
                    // TODO print output and cost
                    log = log.substring(0, log.length() - 1);
					//System.out.println(log);
                    out.println(log);
                    cost = temp.weight;
                    while(temp != null){
                        path = temp.name+"-"+path;
                        temp = temp.parent;
                    }
                    path = path1 + path;
                    path = path.substring(0, path.length() - 1);
					//System.out.println(path);
                    out.println(path);
					//System.out.println(cost);
                    out.println(cost);
                    out.close();
                    return;
                }
                int indexOfTemp = Arrays.asList(store_list).indexOf(temp.name);
                visited1[indexOfTemp] = true;
                for(int i = 0; i < total_nodes; i++){
                    if(!visited1[i] && (Integer.parseInt(store_matrix[indexOfTemp][i]) > 0)){// If neighbour is not explored then visit
                        Node1 newNode = new Node1(store_list[i], (temp.weight + Integer.parseInt(store_matrix[indexOfTemp][i])), temp);
                       if(queue.contains(newNode)){
                           Iterator itr = queue.iterator();
                           while(itr.hasNext()) {
                               Node1 element = (Node1) itr.next();
                               if(element.equals(newNode) && element.weight > newNode.weight){
                                   queue.remove(element);
                                   queue.add(newNode);
                                   break;
                               }
                           }
                       } else {
                           queue.add(newNode);
                       }
                    }
                }
            }
            catch(Exception e2)
            {
                System.out.println(e2);
            }
    }       
 }
    /**
     * @param args the command line arguments
     */
        public static void main(String[] args) throws Exception
        {
           // TODO code application logic here
            int counter = 0;
            String search_strategy = "";
            int i = 0;
            int matrix_row = 0;
            String data = "";
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
                        search_strategy = "bfs";
                    }
                    if(data.equals("2"))
                    {
                        search_strategy = "dfs";
                    }  
                    if(data.equals("3"))
                    {
                        search_strategy = "ucs";
                    }
                }
                if(counter == 1)
                {
                    source = data;
                    //System.out.println("Source is :");
                    //System.out.print(" "+source);
                }
                if(counter == 2)
                {
                    destination = data;
                    //System.out.println("Destination is :");
                    //System.out.print(" "+destination);
                }
                if(counter == 3)
                {
                    total_nodes = Integer.parseInt(data);
                    //System.out.println("Num of nodes :");
                    //System.out.print(" "+total_nodes);
                    store_list = new String[total_nodes];    
                    store_matrix = new String[total_nodes][total_nodes];
                }
                if(counter == 4)
                {
                    store_list[i] = data;
                    i = i + 1;
                }
                if(counter > 4 && (counter < (4+total_nodes)))
                {
                    store_list[i] = data;
                    i = i + 1;
                }
                if(counter >= (4 + total_nodes) && counter < (4 + total_nodes +total_nodes))
                {
                    String temp[] = data.split(" ");
                    for(int i1 = 0; i1< temp.length; i1++)
                    {
                        store_matrix[matrix_row][i1] = temp[i1];
                        //System.out.println("matrix_row: "+matrix_row+"i1: "+i1+"temp[i1]: "+temp[i1]);
                    }
                    matrix_row = matrix_row + 1;
                }
                data = br.readLine();
                counter = counter + 1;
            }
            if(search_strategy.equals("bfs"))
            {
                agent p = new agent();
                p.bfs1();
            }
            if(search_strategy.equals("dfs"))
            {
                agent p = new agent();
                p.dfs1();
            }
            if(search_strategy.equals("ucs"))
            {
                agent p = new agent();
                p.ucs1();
            }
        }
}
