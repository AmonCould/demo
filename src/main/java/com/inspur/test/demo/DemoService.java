package com.inspur.test.demo;

import cn.hutool.core.util.XmlUtil;
import com.inspur.test.demo.factory.verifyInterface;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import com.alibaba.fastjson.JSONObject;
import com.thoughtworks.xstream.XStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import util.SelfResponse;

import javax.persistence.EntityManager;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DemoService implements verifyInterface {
    @Autowired
    DemoRepository demoRepository;

    /*
     * description: list steam操作
     * author: jiangyf
     * date: 2023/4/1 14:02
     * @param
     * @return util.SelfResponse
     */
    public SelfResponse arrayList() {
        List<DemoEntity> demoList = generateList(10);

        List<String> idList = demoList.stream().map(DemoEntity::getId).distinct().collect(Collectors.toList());
        // 逗号分隔
        String idComma = idList.stream().map(String::valueOf).collect(Collectors.joining(","));
        // 分号分隔
        String idSemicolon = idList.stream().map(String::valueOf).collect(Collectors.joining(";"));
        // 拼接单引号
        String idQuotation = idList.stream().map(String::valueOf).collect(Collectors.joining("','"));
        // 拼接到SQL中
        String selfSql = "SELECT ID,CODE,NAME FROM TABLE WHERE ID IN ('" + idQuotation + "')";

        Map<String, Object> rtnMap = new HashMap<>();
        rtnMap.put("idComma", idComma);
        rtnMap.put("idSemicolon", idSemicolon);
        rtnMap.put("idQuotation", idQuotation);
        rtnMap.put("selfSql", selfSql);
        return SelfResponse.createBySuccessWithDataAndMsg("成功", rtnMap);
    }

    /*
     * description: 数组的交、并、差集处理
     * author: jiangyf
     * date: 2023/4/10 15:28
     * @param
     * @return util.SelfResponse
     */
    public SelfResponse arrayListHandel() {
        // 数据组织
        List<DemoEntity> deptListOld = generateList4Ahadle();
        List<DemoEntity> deptListNew = generateList4Bhadle();


        return SelfResponse.createBySuccessWithMsg("123");

    }


    /*
     * description: 对象到Xml
     * author: jiangyf
     * date: 2023/4/2 9:40
     * @param json
     * @return util.SelfResponse
     */
    public SelfResponse xmlTransform(Map<String, Object> json) {
        if (StringUtils.isEmpty(json.get("proName").toString())) {
            return SelfResponse.createByErrorWithMsg("实体名称为空");
        }
//        try {
//            Object object = Class.forName(json.get("proName").toString()).newInstance();
//        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
//            throw new RuntimeException(e);
//        }
        // 组织数据
        List<DemoEntity> demoList = generateList4Bhadle();
        XStream xstream = new XStream();
        XStream.setupDefaultSecurity(xstream);
        xstream.alias("organization", List.class);
        xstream.alias("department", DemoEntity.class);
        StringWriter writer = new StringWriter();
        writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
        xstream.toXML(demoList, writer);
        String xmlStr = writer.toString();

        return SelfResponse.createBySuccessWithData(xmlStr);
    }


    /*
     * description: 数据校验
     * author: jiangyf
     * date: 2023/4/2 9:40
     * @param json
     * @return util.SelfResponse
     */
    public SelfResponse verifyData(Map<String, Object> json) {
        String tyep = "";
        String value = "";
        // 为空、
        verifyInput(tyep, value);
        return SelfResponse.createBySuccess();

    }

    /*
     * description: BigDecimal初始化
     * author: jiangyf
     * date: 2023/4/2 13:20
     * @param
     * @return util.SelfResponse
     */
    public SelfResponse decimalCal() {
        // TODO valueOf >= ZERO >>new. 初始化时优先使用valueof(),避免使用new BigDecimal进行初始化
        Map<String, Object> rtnMap = new TreeMap<>();
        // 1-BigDecimal初始化 -ZERO
        long time1 = System.currentTimeMillis();
        for (int i = 0; i < 300000; i++) {
            BigDecimal bigZero = BigDecimal.ZERO;
        }
        long time2 = System.currentTimeMillis();
        long zeroMs = time2 - time1;
        rtnMap.put("1-ZERO耗时", zeroMs + "毫秒");

        // 2-BigDecimal初始化 - new
        long time3 = System.currentTimeMillis();
        for (int i = 0; i < 300000; i++) {
            BigDecimal bigZero = new BigDecimal(0);
        }
        long time4 = System.currentTimeMillis();
        long newMs = time4 - time3;
        rtnMap.put("2-new耗时", newMs + "毫秒");

        // 3-BigDecimal初始化 - valueOf
        long time5 = System.currentTimeMillis();
        for (int i = 0; i < 300000; i++) {
            BigDecimal bigZero = BigDecimal.valueOf(0);
        }
        long time6 = System.currentTimeMillis();
        long valueofMs = time6 - time5;

        // new BigDecimal方式的数值差异
        double testnum = 11.3;
        BigDecimal valueOfDecimal = BigDecimal.valueOf(testnum);
        BigDecimal newDecimalDou = new BigDecimal(testnum);
        BigDecimal newDecimalStr = new BigDecimal("11.3");

        log.info("------------Start-----------");
        log.info("BigDecimal.valueOf方式的值：" + valueOfDecimal);
        log.info("newDecimal方式的值：" + newDecimalDou);
        log.info("newDecimalStr方式的值：" + newDecimalStr);
        log.info("valueofMs:" + valueofMs + "毫秒");
        log.info("zeroMs:" + zeroMs + "毫秒");
        log.info("newMs:" + newMs + "毫秒");
        log.info("------------End-----------");
        rtnMap.put("3-valueOf耗时", valueofMs + "毫秒");
        rtnMap.put("4-ZERO与valueof耗时比例", zeroMs / valueofMs == 0 ? 1 : valueofMs);
        rtnMap.put("5-new与valueof耗时比例", newMs / valueofMs == 0 ? 1 : valueofMs);
        rtnMap.put("6-BigDecimal.valueOf方式的值", valueOfDecimal);
        rtnMap.put("7-new BigDecimal4Double方式的值", newDecimalDou);
        rtnMap.put("8-new BigDecimal4Str方式的值", newDecimalStr);
        return SelfResponse.createBySuccessWithData(rtnMap);
    }

    /*
     * description: BigDecimal和double运算对比
     * author: jiangyf
     * date: 2023/4/13 17:21
     * @param input1
     * @param input2
     * @param runTimes
     * @return util.SelfResponse
     */
    public SelfResponse decimalBattle(String input1, String input2, String runTimes) {
        Map<String, Object> rtnMap = new TreeMap<>();
        double num1 = Double.parseDouble(input1);
        double num2 = Double.parseDouble(input2);
        double calTimes = Double.parseDouble(runTimes);

        // double运算
        long time1 = System.currentTimeMillis();
        double rstDouble = calByDouble(calTimes, num1, num2);
        long time2 = System.currentTimeMillis();
        long doubleMs = time2 - time1;

        // region Bigdecimal运算
        BigDecimal decInput1 = BigDecimal.valueOf(num1);
        BigDecimal decInput2 = BigDecimal.valueOf(num2);
        long time3 = System.currentTimeMillis();
        BigDecimal rstDecimal = calByBigdecimal(calTimes, decInput1, decInput2);
        long time4 = System.currentTimeMillis();
        long decimalMs = time4 - time3;
        // endregion Bigdecimal运算

        // region 将Bigdecimal转换为double进行运算，然后转换为BigDecimal输出
        long time5 = System.currentTimeMillis();
        // 模拟从实体中get后转换为double
        double double3 = BigDecimal.valueOf(num1).doubleValue();
        double double4 = BigDecimal.valueOf(num2).doubleValue();
        // 模拟进行计算
        double rstTranDouble = calByDouble(calTimes, double3, double4);
        log.info("模拟运算后double的结果：" + rstTranDouble);
        // 模拟将结果转换为BigDecimal然后set到实体
        BigDecimal tranDecimal = BigDecimal.valueOf(rstTranDouble);

        long time6 = System.currentTimeMillis();
        long transformMs = time6 - time5;
        // endregion 将Bigdecimal转换为double进行运算，然后转换为BigDecimal输出

        log.info("------------Start-----------");
        log.info("运算次数：" + calTimes);
        log.info("double运算结果:" + rstDouble);
        log.info("double运算耗时:" + doubleMs);
        log.info("Bigdecimal运算结果:" + rstDecimal);
        log.info("Bigdecimal运算耗时:" + decimalMs);
        log.info("double转换Bigdecimal耗时:" + transformMs);
        log.info("double转换Bigdecimal结果:" + tranDecimal);
        log.info("------------End-----------");

        rtnMap.put("1-运算次数", calTimes + "次");
        rtnMap.put("2-double运算结果", rstDouble);
        rtnMap.put("3-double运算耗时", doubleMs + "毫秒");
        rtnMap.put("4-Bigdecimal运算结果", rstDecimal);
        rtnMap.put("5-Bigdecimal运算耗时", decimalMs + "毫秒");
        rtnMap.put("6-double转换Bigdecimal耗时", transformMs + "毫秒");
        rtnMap.put("7-double转换Bigdecimal结果", tranDecimal);
        return SelfResponse.createBySuccessWithData(rtnMap);
    }

    /*
     * description: double运算
     * author: jiangyf
     * date: 2023/4/13 17:20
     * @param calTimes
     * @param input1
     * @param input2
     * @return double
     */
    public double calByDouble(double calTimes, double input1, double input2) {
        double rstDouble = 0;
        for (int i = 0; i < calTimes; i++) {
            rstDouble += input1 * input2 / input1;
        }
        return rstDouble;
    }

    /*
     * description: Bigdecimal运算
     * author: jiangyf
     * date: 2023/4/13 17:20
     * @param calTimes
     * @param input1
     * @param input2
     * @return java.math.BigDecimal
     */
    public BigDecimal calByBigdecimal(double calTimes, BigDecimal input1, BigDecimal input2) {
        BigDecimal rstDecimal = BigDecimal.valueOf(0);
        for (int i = 0; i < calTimes; i++) {
            rstDecimal = rstDecimal.add(input1.multiply(input2).divide(input1));
        }
        return rstDecimal;
    }

    /*
     * description: 异步更新
     * author: jiangyf
     * date: 2023/4/14 11:42
     * @param
     * @return util.SelfResponse
     */
    @Transactional
    public SelfResponse threadUpdate() {
        List<DemoEntity> demoList = generateList4Thread();
        try {
            demoRepository.saveAll(demoList);
        } catch (RuntimeException e) {
            throw e;
        }

        return SelfResponse.createBySuccessWithMsg("成功");
    }


    public SelfResponse Strsplit(String idStr) {
        String[] splitAtt = idStr.split(",");
        return SelfResponse.createBySuccessWithData(splitAtt);
    }

    /*
     * description: 生成list数据
     * author: jiangyf
     * date: 2023/4/1 14:03
     * @param number
     * @return java.util.List<com.inspur.test.demo.DemoEntity>
     */
    public List<DemoEntity> generateList(int number) {
        List<DemoEntity> demoList = new ArrayList<>();
        for (int count = 0; count <= number; count++) {
            DemoEntity demoEntity = new DemoEntity();
            demoEntity.setId(UUID.randomUUID().toString());
            demoEntity.setCode(String.valueOf(count));
            demoEntity.setName("testDemo");
            demoEntity.setNote("this is a note,put your msg");
            demoList.add(demoEntity);
        }
        return demoList;
    }

    /*
     * description: 生成去年组织部门
     * author: jiangyf
     * date: 2023/4/13 18:08
     * @param
     * @return java.util.List<com.inspur.test.demo.DemoEntity>
     */
    public List<DemoEntity> generateList4Ahadle() {
        List<DemoEntity> deptList = new ArrayList<>();
        DemoEntity demoEntity = new DemoEntity();
        demoEntity.setId(UUID.randomUUID().toString());
        demoEntity.setCode("101");
        demoEntity.setName("生产成本开发组");
        demoEntity.setNote("装备制造事业部");
        deptList.add(demoEntity);

        DemoEntity demoEntity1 = new DemoEntity();
        demoEntity1.setId(UUID.randomUUID().toString());
        demoEntity1.setCode("102");
        demoEntity1.setName("智能工厂开发组");
        demoEntity1.setNote("装备制造事业部");
        deptList.add(demoEntity1);

        DemoEntity demoEntity2 = new DemoEntity();
        demoEntity2.setId(UUID.randomUUID().toString());
        demoEntity2.setCode("103");
        demoEntity2.setName("项目资产开发组");
        demoEntity2.setNote("装备制造事业部");
        deptList.add(demoEntity2);

        DemoEntity demoEntity3 = new DemoEntity();
        demoEntity3.setId(UUID.randomUUID().toString());
        demoEntity3.setCode("104");
        demoEntity3.setName("供应链开发组");
        demoEntity3.setNote("智慧管控事业部");
        deptList.add(demoEntity3);

        DemoEntity demoEntity4 = new DemoEntity();
        demoEntity4.setId(UUID.randomUUID().toString());
        demoEntity4.setCode("105");
        demoEntity4.setName("财务开发组");
        demoEntity4.setNote("智慧管控事业部");
        deptList.add(demoEntity4);

        DemoEntity demoEntity5 = new DemoEntity();
        demoEntity5.setId(UUID.randomUUID().toString());
        demoEntity5.setCode("106");
        demoEntity5.setName("数据与集成组");
        demoEntity5.setNote("智慧管控事业部");
        deptList.add(demoEntity5);

        DemoEntity demoEntity6 = new DemoEntity();
        demoEntity6.setId(UUID.randomUUID().toString());
        demoEntity6.setCode("100");
        demoEntity6.setName("船舶与装备事业部");
        demoEntity6.setNote("战略大客户事业部");
        deptList.add(demoEntity6);

        return deptList;
    }

    /*
     * description: 生成今年组织部门
     * author: jiangyf
     * date: 2023/4/13 18:08
     * @param
     * @return java.util.List<com.inspur.test.demo.DemoEntity>
     */
    public List<DemoEntity> generateList4Bhadle() {
        List<DemoEntity> deptList = new ArrayList<>();
        DemoEntity demoEntity = new DemoEntity();
        demoEntity.setId(UUID.randomUUID().toString());
        demoEntity.setCode("101");
        demoEntity.setName("生产成本开发组");
        demoEntity.setNote("装备制造事业部");
        deptList.add(demoEntity);

        DemoEntity demoEntity1 = new DemoEntity();
        demoEntity1.setId(UUID.randomUUID().toString());
        demoEntity1.setCode("102");
        demoEntity1.setName("智能工厂开发组");
        demoEntity1.setNote("装备制造事业部");
        deptList.add(demoEntity1);

        DemoEntity demoEntity2 = new DemoEntity();
        demoEntity2.setId(UUID.randomUUID().toString());
        demoEntity2.setCode("103");
        demoEntity2.setName("项目资产开发组");
        demoEntity2.setNote("装备制造事业部");
        deptList.add(demoEntity2);

        DemoEntity demoEntity3 = new DemoEntity();
        demoEntity3.setId(UUID.randomUUID().toString());
        demoEntity3.setCode("104");
        demoEntity3.setName("供应链开发组");
        demoEntity3.setNote("装备制造事业部");
        deptList.add(demoEntity3);

        DemoEntity demoEntity4 = new DemoEntity();
        demoEntity4.setId(UUID.randomUUID().toString());
        demoEntity4.setCode("105");
        demoEntity4.setName("财务开发组");
        demoEntity4.setNote("装备制造事业部");
        deptList.add(demoEntity4);

        DemoEntity demoEntity5 = new DemoEntity();
        demoEntity5.setId(UUID.randomUUID().toString());
        demoEntity5.setCode("106");
        demoEntity5.setName("数据与集成组");
        demoEntity5.setNote("装备制造事业部");
        deptList.add(demoEntity5);

        DemoEntity demoEntity6 = new DemoEntity();
        demoEntity6.setId(UUID.randomUUID().toString());
        demoEntity6.setCode("100");
        demoEntity6.setName("装备制造事业部");
        demoEntity6.setNote("战略大客户事业部");
        deptList.add(demoEntity6);

        return deptList;
    }

    /*
     * description: 生成线程数组
     * author: jiangyf
     * date: 2023/4/14 11:47
     * @param
     * @return java.util.List<com.inspur.test.demo.DemoEntity>
     */
    public List<DemoEntity> generateList4Thread() {
        List<DemoEntity> demoList = new ArrayList<>();
        for (int i = 0; i <= 10000; i++) {
            DemoEntity demoEntity = new DemoEntity();
            demoEntity.setId(String.valueOf(i));
            demoEntity.setCode("101");
            demoEntity.setName("生产成本开发组");
            demoEntity.setNote("装备制造事业部");
            demoList.add(demoEntity);
        }
        return demoList;
    }


    @Override
    public String verifyInput(String type, String value) {
        return null;
    }

    /*
     * description: 实体更新持久态验证
     * author: jiangyf
     * date: 2024/2/4 14:15
     * @param text
     * 1) 去掉@Transactional，使用@Query进行实体查询不会触发更新；
     * 2) 保留@Transactional，使用@Query查询只要返回实体仍会触发更新；
     * @return util.SelfResponse
     */
    @Transactional
    public SelfResponse EntitySetTest(String text) {

        // 第一种。查询testDemo表的Cost（会更新数据库） nativeQuery = false
        List<DemoEntity> demoEntityList = demoRepository.findCostByID(text);


        // 第二种，查询testDemo表的Cost（会更新数据库） nativeQuery = true
//        List<DemoEntity> demoEntityList = demoRepository.findCostByIDNoEntity(text);

        // 解决方案：复制实体由持久态转为瞬态
//        List<DemoEntity> demoEntityListNew = new ArrayList<>();
//        BeanUtils.copyProperties(demoEntityList, demoEntityListNew);

        for (DemoEntity demo : demoEntityList) {
            demo.setCost(BigDecimal.valueOf(8888888));
            demo.setBalance(BigDecimal.valueOf(999999));
        }

        // 解决方案：递归新实体
//        for (DemoEntity demo : demoEntityListNew) {
//            demo.setCost(BigDecimal.valueOf(8888888));
//            demo.setBalance(BigDecimal.valueOf(999999));
//        }

        return SelfResponse.createBySuccessWithMsg("执行完成");
    }

}
