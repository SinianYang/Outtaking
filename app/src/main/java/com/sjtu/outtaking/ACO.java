package com.sjtu.outtaking;
import com.sjtu.outtaking.ant;

import java.io.*;
/**
 *蚁群优化算法，用来求解TSP问题
 * @author FashionXu
 */
public class ACO {
    //定义蚂蚁群
    ant []ants;
    int antcount;//蚂蚁的数量
    int [][]distance;//表示城市间距离
    double [][]tao;//信息素矩阵
    int citycount;//城市数量
    int[]besttour;//求解的最佳路径
    int bestlength;//求的最优解的长度
    int[] resulttour;
    /** 初始化函数
     //*@param filename tsp数据文件
     *@param antnum 系统用到蚂蚁的数量
    //*@throws 如果文件不存在则抛出异常
     */
    public void init(int dist[][],int antnum){
        antcount=antnum;
        ants=new ant[antcount];
        //读取数据
        int[] x;
        int[] y;
//        String strbuff;
//        BufferedReader tspdata = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
//        strbuff = tspdata.readLine();
        citycount = dist.length;
        distance = dist;
        x = new int[citycount];
        y = new int[citycount];
//        for (int citys = 0; citys < citycount; citys++) {
//            strbuff = tspdata.readLine();
//            String[] strcol = strbuff.split(" ");
//            x[citys] = Integer.valueOf(strcol[1]);
//            y[citys] = Integer.valueOf(strcol[2]);
//        }
//        //计算距离矩阵
//        for (int city1 = 0; city1 < citycount - 1; city1++) {
//            distance[city1][city1] = 0;
//            for (int city2 = city1 + 1; city2 < citycount; city2++) {
//                distance[city1][city2] = (int) (Math.sqrt((x[city1] - x[city2]) * (x[city1] - x[city2])
//                        + (y[city1] - y[city2]) * (y[city1] - y[city2])) + 0.5);
//                distance[city2][city1] = distance[city1][city2];
//            }
//        }
//        distance[citycount - 1][citycount - 1] = 0;
        //初始化信息素矩阵
        tao=new double[citycount][citycount];
        for(int i=0;i<citycount;i++)
        {
            for(int j=0;j<citycount;j++){
                tao[i][j]=0.1;
            }
        }
        bestlength=Integer.MAX_VALUE;
        besttour=new int[citycount+1];
        //随机放置蚂蚁
        for(int i=0;i<antcount;i++){
            ants[i]=new ant();
            ants[i].RandomSelectCity(citycount);
        }
    }
    /**
     * ACO的运行过程
     * @param maxgen ACO的最多循环次数
     *
     */
    public void run(int maxgen){
        for(int runtimes=0;runtimes<maxgen;runtimes++){

            //每一只蚂蚁移动的过程
            for(int i=0;i<antcount;i++){
                for(int j=1;j<citycount;j++){
                    ants[i].SelectNextCity(j,tao,distance);
                }
                //计算蚂蚁获得的路径长度
                ants[i].CalTourLength(distance);
                //System.out.print("I am runing......");
                //System.out.print(ants[i].tourlength);
                //System.out.print("\n");
                if(ants[i].tourlength<bestlength){
                    //保留最优路径
                    bestlength=ants[i].tourlength;
                    System.out.println("In "+runtimes+" times, find the newest result"+bestlength);
                    for(int j=0;j<citycount+1;j++)
                        besttour[j]=ants[i].tour[j];
                }
            }
            //更新信息素矩阵
            UpdateTao();
            //重新随机设置蚂蚁
            for(int i=0;i<antcount;i++){
                ants[i].RandomSelectCity(citycount);
            }
        }
    }
    /**
     * 更新信息素矩阵
     */
    private void UpdateTao(){
        double rou=0.5;
        //信息素挥发
        for(int i=0;i<citycount;i++)
            for(int j=0;j<citycount;j++)
                tao[i][j]=tao[i][j]*(1-rou);
        //信息素更新
        for(int i=0;i<antcount;i++){
            for(int j=0;j<citycount;j++){
                tao[ants[i].tour[j]][ants[i].tour[j+1]]+=1.0/ants[i].tourlength;
            }
        }
    }
    /**
     * 输出程序运行结果
     */
    public void ReportResult(){
        System.out.println("The best length is "+bestlength);
        int x = 0;
        for (int i = 0; i<besttour.length;i++){
            if(besttour[i] == 0){
                x = i;
            }

        }
        int[]newtour=new int[besttour.length];
        resulttour = new int[besttour.length];
        for (int i = 0; i<besttour.length;i++){
            if(i+x<besttour.length){
                newtour[i]=besttour[i+x];
            }
            else if(i+x>= besttour.length){
                newtour[i]=besttour[i-(besttour.length-x)+1];
            }

        }

        for (int i = 0; i<besttour.length;i++){
            resulttour[i] = newtour[i];
            System.out.println(newtour[i]);
        }

    }

    public int[] getResulttour(){
        return resulttour;
    }

}
