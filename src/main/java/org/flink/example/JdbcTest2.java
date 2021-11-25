package org.flink.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class JdbcTest2 {
    public static int getRan() {
        int max=100,min=1;
        long randomNum = System.currentTimeMillis();
        int ran3 = (int) (randomNum%(max-min)+min);
        return ran3;
    }
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Connection conn = null;
        PreparedStatement pst = null;
        String[] pnames = {"qq.com.rpk","didi.com.rpk","toutiao.com.rpk","baidu.com.rpk","douyin.com.rpk","kugou.com.rpk","4399.com.rpk"};
        String[] states = {"starting","running", "completed"};
        try {

            //加载驱动程序
            Class.forName("com.mysql.jdbc.Driver");

            //创建连接
            //java10为数据库名
            String url="jdbc:mysql://172.20.187.21:3306/db_flink_cdc?useSSL=false";
            String username="root";
            String userpwd="Middle@2020";
            String sql = "INSERT INTO t_cdc_1 (id, pname, s_state, s_value, c_value) values(?,?,?,?,?)";
            conn = DriverManager.getConnection(url,username,userpwd);

            int pnamesLen = pnames.length;
            int stateLen = states.length;
            conn.setAutoCommit(false);
            pst=conn.prepareStatement(sql);
            for (int i = 3200001; i <= 3500000; i++) {
                pst.setInt(1, i);
                pst.setString(2, pnames[i%pnamesLen]);
                pst.setString(3, states[i%stateLen]);
                pst.setInt(4,getRan()+i);
                pst.setInt(5,1);
                pst.addBatch();

                if (i % 10000 == 0) {
                    pst.executeBatch();
                    conn.commit();
                    System.out.println("commit:" + i);
                }
            }
            pst.executeBatch();
            conn.commit();
            System.out.println("end");

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }

                if (conn !=null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}