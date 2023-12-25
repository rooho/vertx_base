package cc.phonecard.response;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LogisticsResp implements Serializable {

  private static final long serialVersionUID = 2507811643115809066L;

  /**
   * 物流公司
   * */
  private String lgstcsCmpny;

  /**
   * 物流单号
   * */
  private String lgstcsOrderNo;

  /**
   * 订单编号
   * */
  private String orderNo;

  /**
   * 证件名称
   * */
  private String idCardName;

  /**
   * 证件号码
   * */
  private String idCardNo;

  /**
   * 联系电话
   * */
  private String phoneNum;

  /**
   * 订单状态
   * */
  private Integer orderStatus;

  /**
   * 收货状态(1-待发货,2-已发货,3-已收件)
   * */
  private String receiveStatus;

  /**
   * 下单商品
   * */
  private String orderProducts;

}
