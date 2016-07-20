package program;

import net.sf.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by xiaojing on 2016/7/20.
 */
public class TotalPrice {
    public static float totalPri() {
        ArrayList<JSONObject> input = TotalPrice.createData();
        //商品信息
        HashMap<String,Goods> data = new HashMap<String,Goods>();
        for(Object tmp:input){
            JSONObject jsonObject = JSONObject.fromObject(tmp);
            Goods a  = (Goods) JSONObject.toBean(jsonObject,Goods.class);
            data.put(a.getBarcode(), a);
        }
        System.out.println("商品信息"+data.toString());
        //售出商品信息
        ArrayList<String> sells = new ArrayList<String>();
        sells = TotalPrice.createSellData();
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap = TotalPrice.parseSellData(sells);
        System.out.println("售出商品信息"+hashMap.toString());
        //计算
        HashSet<String> zhe95set = new HashSet<String>();
        zhe95set = TotalPrice.zhe95();
        HashSet<String> buyTwoset = new HashSet<String>();
        buyTwoset = TotalPrice.buyTwo();
        String output = " ***<没钱赚商店>购物清单***\n ";
        DecimalFormat decimalFormat=new DecimalFormat("0.00");
        float totalPrice = (float) 0.0; //总共花了多少钱
        float saveMoney = (float) 0.0; //总共节省了多少钱
        String outBuyTwo = "----------------\n买二赠一商品：\n";//买二赠一商品的输出字符串
        for (Map.Entry<String, String> entry : hashMap.entrySet()) {
            if(buyTwoset.contains(entry.getKey())){
                Goods goods = data.get(entry.getKey());
                totalPrice += (Float.parseFloat( entry.getValue() ))*(goods.getPrice());

                int num = Integer.parseInt( entry.getValue() );
                int zeng = num / 3 ;//赠送的数量
                int truenum = num - zeng ; //实际收钱的数量

                saveMoney += goods.getPrice() * zeng;
                output = output + "名称："+goods.getName()
                        +",数量："+ entry.getValue()
                        +",单价："+decimalFormat.format( goods.getPrice() )
                        +",小计："+ decimalFormat.format( truenum* goods.getPrice())
                        +" \n";
                outBuyTwo = outBuyTwo + "名称："+goods.getName()
                        +  ",数量："+ zeng
                        + "\n";
            }else if(zhe95set.contains(entry.getKey())){
                Goods goods = data.get(entry.getKey());
                totalPrice += (Float.parseFloat( entry.getValue() ))*(goods.getPrice());
                saveMoney += ((Float.parseFloat( entry.getValue() ))*(goods.getPrice())) * 0.05;
                output = output + "名称："+goods.getName()
                        +",数量："+ entry.getValue()
                        +",单价："+decimalFormat.format( goods.getPrice() )
                        +",小计："+ decimalFormat.format( (Float.parseFloat( entry.getValue() ))*(goods.getPrice())*0.95 )
                        +",节省："+ decimalFormat.format( (Float.parseFloat( entry.getValue() ))*(goods.getPrice())*0.05 )
                        +"\n";
            }else{
                Goods goods = data.get(entry.getKey());
                output = output + "名称："+goods.getName()
                        +",数量："+ entry.getValue()
                        +",单价："+decimalFormat.format( goods.getPrice() )
                        +",小计："+decimalFormat.format( (Float.parseFloat( entry.getValue() ))*(goods.getPrice()) )
                        +"\n";
            }
        }
        String total = "-----------------\n";
        total = total + "总计："+totalPrice + "\n节省：" + saveMoney + "\n******************";//输出统计信息
        System.out.println(output + outBuyTwo + total);
        return totalPrice;
    }
    /**
     *返回打95折商品的集合,商家规定的打折商品的集合
     */
    public static HashSet<String> zhe95(){
        HashSet<String> set = new HashSet<String>();
        set.add("ITEM000001");
        return set;
    }
    /**
     * 返回买二赠一商品的集合，商家规定的
     */
    public static HashSet<String> buyTwo(){
        HashSet<String> set = new HashSet<String>();
        set.add("ITEM000005");
        set.add("ITEM000003");
        return set;
    }
    /**
     * 返回打折商品的集合，规则是购买某种商品价格超过10元就打折
     * @param hashMap
     * @param data
     * @return
     */
    public static HashSet<String> zheGoods(HashMap<String, String> hashMap,HashMap<String,Goods> data){
        HashSet<String> set = new HashSet<String>();
        int sum = 0;
        for (Map.Entry<String, String> entry : hashMap.entrySet()) {
            Goods goods = data.get(entry.getKey());
            sum = Integer.parseInt( entry.getValue() );
            for (Map.Entry<String, String> en : hashMap.entrySet()) {
                if(!entry.getKey().equals(en.getKey())){
                    Goods g = data.get(en.getKey());
                    if(goods.getCategory().equals(g.getCategory())){
                        if(goods.getSubCategory().equals(g.getSubCategory())){
                            sum = Integer.valueOf(  entry.getValue() ).intValue() + Integer.valueOf(  en.getValue() ).intValue()  + sum;
                        }
                    }
                }
            }
            if(sum >= 10){
                set.add(entry.getKey());
            }
            sum = 0;
        }
        System.out.println("打折的商品是："+set.toString());
        return set;
    }
    /**
     * 解析售出商品的数据
     * @param
     * @return
     */
    public static HashMap<String, String> parseSellData(ArrayList<String> sells){
        HashMap<String, String> hashMap = new HashMap<String, String>();
        for(String tmp:sells){
            String[] aa = tmp.split("-");
            if(aa.length == 1){
                if(hashMap.containsKey(aa[0])){
                    hashMap.replace(aa[0],  String.valueOf( Integer.parseInt( hashMap.get(aa[0]) )+1 ) ) ;
                }else{
                    hashMap.put(aa[0], "1");
                }
            }else if(aa.length == 2){
                if(hashMap.containsKey(aa[0])){
                    hashMap.replace(aa[0],  String.valueOf( Integer.parseInt( hashMap.get(aa[0]) )+Integer.parseInt(aa[1]) ) ) ;
                }else{
                    hashMap.put(aa[0], aa[1]);
                }
            }else{
                System.out.println("售出商品格式错误");
            }
        }
        return hashMap;
    }

    /**
     * 生成卖出数据
     * @return
     */
    public static ArrayList<String> createSellData(){
        ArrayList<String> selles = new ArrayList<String>();
        selles.add("ITEM000001");
        selles.add("ITEM000001");
        selles.add("ITEM000001");
        selles.add("ITEM000003-20");
        selles.add("ITEM000005-5");
        selles.add("ITEM000006-5");
        return selles;
    }
    /**
     * 生成卖出数据
     * @return
     */
    public static ArrayList<JSONObject>  createData(){
        ArrayList<JSONObject> data = new ArrayList<JSONObject>();

        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("barcode", "ITEM000003");
        jsonObject1.put("name", "可口可乐");
        jsonObject1.put("unit", "瓶");
        jsonObject1.put("category", "食品");
        jsonObject1.put("subCategory", "碳酸饮料");
        jsonObject1.put("price", 3.00);
        data.add(jsonObject1);

        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("barcode", "ITEM000001");
        jsonObject2.put("name", "雪花啤酒");
        jsonObject2.put("unit", "瓶");
        jsonObject2.put("category", "食品");
        jsonObject2.put("subCategory", "酒");
        jsonObject2.put("price", 3.00);
        data.add(jsonObject2);

        JSONObject jsonObject3 = new JSONObject();
        jsonObject3.put("barcode", "ITEM000005");
        jsonObject3.put("name", "小浣熊");
        jsonObject3.put("unit", "包");
        jsonObject3.put("category", "食品");
        jsonObject3.put("subCategory", "零食");
        jsonObject3.put("price", 3.00);
        data.add(jsonObject3);

        JSONObject jsonObject4 = new JSONObject();
        jsonObject4.put("barcode", "ITEM000006");
        jsonObject4.put("name", "康师傅方便面");
        jsonObject4.put("unit", "包");
        jsonObject4.put("category", "食品");
        jsonObject4.put("subCategory", "零食");
        jsonObject4.put("price", 3.00);
        data.add(jsonObject4);

        return data;
    }
}
