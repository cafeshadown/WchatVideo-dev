package com.video.service.impl;

import com.video.mapper.UsersLikeVideosMapper;
import com.video.mapper.UsersMapper;
import com.video.pojo.Users;
import com.video.pojo.UsersLikeVideos;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * 基于用户的协同过滤推荐算法实现
 * A a b d
 * B a c
 * C b e
 * D c d e
 *
 * @author Administrator
 */
@Component
public class UserCF {

    @Resource
    private
    UsersMapper usersMapper;
    @Resource
    private
    UsersLikeVideosMapper usersLikeVideosMapper;

//    public static void main(String[] args) {
//        /**
//         * 输入用户-->视频条目  一个用户对应多个物品
//         * 用户ID	喜欢的ID集合
//         *   A		a b d
//         *   B		a c
//         *   C		b e
//         *   D		c d e
//         */
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Input the total users number:");
//        //输入用户总量
//        int N = scanner.nextInt();
//        int[][] sparseMatrix = new int[N][N];//建立用户稀疏矩阵，用于用户相似度计算【相似度矩阵】
//        Map<String, Integer> userItemLength = new HashMap<>();//存储每一个用户对应的不同物品总数  eg: A 3
//        Map<String, Set<String>> itemUserCollection = new HashMap<>();//建立物品到用户的倒排表 eg: a A B
//        Set<String> items = new HashSet<>();//辅助存储物品集合
//        Map<String, Integer> userID = new HashMap<>();//辅助存储每一个用户的用户ID映射
//        Map<Integer, String> idUser = new HashMap<>();//辅助存储每一个ID对应的用户映射
//        System.out.println("Input user--items maping infermation:<eg:A a b d>");
//        scanner.nextLine();
//        for (int i = 0; i < N; i++) {//依次处理N个用户 输入数据  以空格间隔
//            String[] user_item = scanner.nextLine().split(" ");
//            int length = user_item.length;
//            userItemLength.put(user_item[0], length - 1);//eg: A 3
//            userID.put(user_item[0], i);//用户ID与稀疏矩阵建立对应关系
//            idUser.put(i, user_item[0]);
//            //建立物品--用户倒排表
//            for (int j = 1; j < length; j++) {
//                if (items.contains(user_item[j])) {//如果已经包含对应的物品--用户映射，直接添加对应的用户
//                    itemUserCollection.get(user_item[j]).add(user_item[0]);
//                } else {//否则创建对应物品--用户集合映射
//                    items.add(user_item[j]);
//                    itemUserCollection.put(user_item[j], new HashSet<String>());//创建物品--用户倒排关系
//                    itemUserCollection.get(user_item[j]).add(user_item[0]);
//                }
//            }
//        }
//        System.out.println(itemUserCollection.toString());
//        //计算相似度矩阵【稀疏】
//        Set<Entry<String, Set<String>>> entrySet = itemUserCollection.entrySet();
//        Iterator<Entry<String, Set<String>>> iterator = entrySet.iterator();
//        while (iterator.hasNext()) {
//            Set<String> commonUsers = iterator.next().getValue();
//            for (String user_u : commonUsers) {
//                for (String user_v : commonUsers) {
//                    if (user_u.equals(user_v)) {
//                        continue;
//                    }
//                    sparseMatrix[userID.get(user_u)][userID.get(user_v)] += 1;//计算用户u与用户v都有正反馈的物品总数
//                }
//            }
//        }
//        System.out.println(userItemLength.toString());
//        System.out.println("Input the user for recommendation:<eg:A>");
//        String recommendUser = scanner.nextLine();
//        System.out.println(userID.get(recommendUser));
//        //计算用户之间的相似度【余弦相似性】
//        int recommendUserId = userID.get(recommendUser);
//        for (int j = 0; j < sparseMatrix.length; j++) {
//            if (j != recommendUserId) {
//                System.out.println(idUser.get(recommendUserId) + "--" + idUser.get(j) + "相似度:" + sparseMatrix[recommendUserId][j] / Math.sqrt(userItemLength.get(idUser.get(recommendUserId)) * userItemLength.get(idUser.get(j))));
//            }
//        }
//
//        //计算指定用户recommendUser的物品推荐度
//        for (String item : items) {//遍历每一件物品
//            Set<String> users = itemUserCollection.get(item);//得到购买当前物品的所有用户集合
//            if (!users.contains(recommendUser)) {//如果被推荐用户没有购买当前物品，则进行推荐度计算
//                double itemRecommendDegree = 0.0;
//                for (String user : users) {
//                    itemRecommendDegree += sparseMatrix[userID.get(recommendUser)][userID.get(user)] / Math.sqrt(userItemLength.get(recommendUser) * userItemLength.get(user));//推荐度计算
//                }
//                System.out.println("The item " + item + " for " + recommendUser + "'s recommended degree:" + itemRecommendDegree);
//            }
//        }
//
//        Map<String,Double> map=new HashMap<>();
//        map.put("A",1.0);
//        map.put("B",2.0);
//        map.put("C",0.0);
//        List<Map.Entry<String,Double>> list = new ArrayList<>(map.entrySet());
//        //升序排序
//        list.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));
//        System.out.println(list.stream().map(Entry::getKey).collect(Collectors.toList()));
//
//        scanner.close();
//    }


    public List<String> UserCFService(String userId) {
        System.out.println("Input the total users number:");
        //输入用户总量
        List<Users> users = usersMapper.selectAll();
        int N = users.size();
        int[][] sparseMatrix = new int[N][N];//建立用户稀疏矩阵，用于用户相似度计算【相似度矩阵】
        Map<String, Integer> userItemLength = new HashMap<>();//存储每一个用户对应的不同物品总数  eg: A 3
        Map<String, Set<String>> itemUserCollection = new HashMap<>();//建立物品到用户的倒排表 eg: a A B
        Set<String> items = new HashSet<>();//辅助存储物品集合
        Map<String, Integer> userID = new HashMap<>();//辅助存储每一个用户的用户ID映射
        Map<Integer, String> idUser = new HashMap<>();//辅助存储每一个ID对应的用户映射
        System.out.println("Input user--items maping infermation:<eg:A a b d>");
        List<UsersLikeVideos> videosList = usersLikeVideosMapper.selectAll();

        for (int i = 0; i < N; i++) {//依次处理N个用户 输入数据  以空格间隔
            String userNumber = users.get(i).getId();
            List<String> userItem = videosList.stream().filter(e -> e.getUserId().equals(userNumber)).map(UsersLikeVideos::getVideoId).collect(Collectors.toList());
            int length = userItem.size();
            userItemLength.put(userNumber, length - 1);//eg: A 3
            userID.put(userNumber, i);//用户ID与稀疏矩阵建立对应关系
            idUser.put(i, userNumber);
            //建立物品--用户倒排表
            for (int j = 0; j < length; j++) {
                if (items.contains(userItem.get(j))) {//如果已经包含对应的物品--用户映射，直接添加对应的用户
                    itemUserCollection.get(userItem.get(j)).add(userNumber);
                } else {//否则创建对应物品--用户集合映射
                    items.add(userItem.get(j));
                    itemUserCollection.put(userItem.get(j), new HashSet<>());//创建物品--用户倒排关系
                    itemUserCollection.get(userItem.get(j)).add(userNumber);
                }
            }
        }
        System.out.println(itemUserCollection.toString());
        //计算相似度矩阵【稀疏】
        Set<Entry<String, Set<String>>> entrySet = itemUserCollection.entrySet();
        Iterator<Entry<String, Set<String>>> iterator = entrySet.iterator();
        while (iterator.hasNext()) {
            Set<String> commonUsers = iterator.next().getValue();
            for (String user_u : commonUsers) {
                for (String user_v : commonUsers) {
                    if (user_u.equals(user_v)) {
                        continue;
                    }
                    sparseMatrix[userID.get(user_u)][userID.get(user_v)] += 1;//计算用户u与用户v都有正反馈的物品总数
                }
            }
        }
        System.out.println(userItemLength.toString());
        System.out.println("Input the user for recommendation:<eg:A>");
        System.out.println(userID.get(userId));
        //计算用户之间的相似度【余弦相似性】
        int recommendUserId = userID.get(userId);
        for (int j = 0; j < sparseMatrix.length; j++) {
            if (j != recommendUserId) {
                System.out.println(idUser.get(recommendUserId) + "--" + idUser.get(j) + "相似度:" + sparseMatrix[recommendUserId][j] / Math.sqrt(userItemLength.get(idUser.get(recommendUserId)) * userItemLength.get(idUser.get(j))));
            }
        }

        Map<String,Double> map = new HashMap<>();
        //计算指定用户recommendUser的物品推荐度
        for (String item : items) {//遍历每一件物品
            Set<String> users1 = itemUserCollection.get(item);//得到购买当前物品的所有用户集合
            if (!users1.contains(userId)) {//如果被推荐用户没有购买当前物品，则进行推荐度计算
                double itemRecommendDegree = 0.0;
                for (String user : users1) {
                    itemRecommendDegree += sparseMatrix[userID.get(userId)][userID.get(user)] / Math.sqrt(userItemLength.get(userId) * userItemLength.get(user));//推荐度计算
                }
                map.put(item,itemRecommendDegree);
                System.out.println("The item " + item + " for " + userId + "'s recommended degree:" + itemRecommendDegree);
            }
        }
        List<Map.Entry<String,Double>> list = new ArrayList<>(map.entrySet());
        //升序排序
        list.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));

return  list.stream().map(Entry::getKey).collect(Collectors.toList());

    }
}