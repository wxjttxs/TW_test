package program;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by xiaojing on 2016/7/20.
 */
public class GoodsTest {
    @Test
    public void should_return_84_given_buy2give1(){
        TotalPrice totalP=new TotalPrice();
        float totalPrice = (float) 0.0; //总共花了多少钱
        totalPrice=totalP.totalPri();
        assertEquals(84.0,totalPrice,0.0);


    }

}
