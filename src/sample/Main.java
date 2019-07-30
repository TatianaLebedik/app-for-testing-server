package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.apache.poi.ss.usermodel.*;

import javax.swing.*;

import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;

public class Main extends Application{

    int port = 53200;
    private static ServerSocket serverSocket;
    private static Controller controller;
    private static Socket socket;
    private static BufferedReader reader;
    private static OutputStream output;
    private static PrintWriter writer;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setResizable(false);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        try {
            serverSocket = new ServerSocket(port);
        }catch (Exception e){
            System.out.println(e);
}
        Thread thread = new Thread(runnable);
        thread.start();

        controller = loader.getController();
        controller.setServerStatusConsole("Server is listening on port " + port + ";\n");
        System.out.println("Server is listening on port " + port);


       // setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
//        addWindowListener(new WindowAdapter(){
//            public void windowClosing(WindowEvent evt){
//               try {
//                   serverSocket.close();
//                   System.out.println("Server stop");
//               }catch (Exception e){
//                   System.out.println("Server not stop");
//                   System.out.println(e);
//               }
//                // тут обрабатываете нажатие на крестик как нравится
//            }
//        });
//

    }


    Runnable runnable = new Runnable() {
        public void run() {
            try {
                while (true) {

                    socket = serverSocket.accept();
                    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    output = socket.getOutputStream();
                    writer = new PrintWriter(output, true);
                    writer.println(new Date().toString());
                    String name = reader.readLine();
                    Thread thread = new Thread(interaction_with_client);
                    thread.start();
                    controller.setServerStatusConsole("+" + name + " is connected;\n");

                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }


    };


    Runnable interaction_with_client = new Runnable() {
        public void run() {
            int i = 1;
            try {
                while (true) {

                    FileInputStream questions_excel_input_stream = new FileInputStream("/home/tatina/IntelliJIDEAProjects/ComputerNetworkServer/excel_files/Test.xls");
                    Workbook workBookOut = new HSSFWorkbook(questions_excel_input_stream);


                    if(workBookOut.getSheetAt(0).getLastRowNum() + 1 == i){
                        System.out.println(workBookOut.getSheetAt(0).getLastRowNum());
                        writer.println("stop");

                        FileInputStream result_excel_input_stream = new FileInputStream("/home/tatina/IntelliJIDEAProjects/ComputerNetworkServer/excel_files/Result.xls");
                        HSSFWorkbook workbookBookIn = new HSSFWorkbook(result_excel_input_stream);
                        HSSFSheet worksheet = workbookBookIn.getSheetAt(0);

                        Row row = worksheet.createRow(worksheet.getLastRowNum() + 1);
                        String name =reader.readLine();
                        row.createCell(0).setCellValue(name);
                        row.createCell(1).setCellValue(reader.readLine());
                        String score = reader.readLine();
                        row.createCell(2).setCellValue(score);

                        result_excel_input_stream.close();
                        if(row.getCell(0).getStringCellValue().isEmpty() || row.getCell(2).getStringCellValue().isEmpty()) {

                        }else{
                            FileOutputStream result_excel_output_stream = new FileOutputStream(new File("/home/tatina/IntelliJIDEAProjects/ComputerNetworkServer/excel_files/Result.xls"));
                            workbookBookIn.write(result_excel_output_stream);
                            result_excel_output_stream.close();
                            controller.setServerStatusConsole("- "+ name + " is successfully passed the test. | Score:" + score + ";\n");
                           // socket.close();
                        }

                        break;
                    }

                        DataFormatter formatter = new DataFormatter();
                        String question = formatter.formatCellValue(workBookOut.getSheetAt(0).getRow(i).getCell(0));
                        String answer1 = formatter.formatCellValue(workBookOut.getSheetAt(0).getRow(i).getCell(1));
                        String answer2 = formatter.formatCellValue(workBookOut.getSheetAt(0).getRow(i).getCell(2));
                        String answer3 = formatter.formatCellValue(workBookOut.getSheetAt(0).getRow(i).getCell(3));
                        String answer4 = formatter.formatCellValue(workBookOut.getSheetAt(0).getRow(i).getCell(4));
                        String correct_answer = formatter.formatCellValue(workBookOut.getSheetAt(0).getRow(i).getCell(5));

                        writer.println(question);
                        writer.println(answer1);
                        writer.println(answer2);
                        writer.println(answer3);
                        writer.println(answer4);
                        writer.println(correct_answer);
                        reader.readLine();
                        i++;

                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    };

    public static void main(String[] args) throws Exception {

        launch(args);

    }
}
