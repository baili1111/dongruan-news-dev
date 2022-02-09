package com.dongruan.article.controller;

import com.dongruan.pojo.Article;
import com.dongruan.pojo.Spouse;
import com.dongruan.pojo.Stu;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

/**
 * @author zhu
 * @date 2022/1/16 17:12:50
 * @description
 */
@Controller
@RequestMapping("free")
public class FreemarkerController {

	@Value("${freemarker.html.target}")
	private String htmlTarget;

	@GetMapping("/createHTML")
	@ResponseBody
	public String createHTML(Model model) throws IOException, TemplateException {

		// 0. 配置freemarker基本环境
		Configuration cfg = new Configuration(Configuration.getVersion());

		// 声明freemarker模板所需要加载的目录的位置
		String classpath = this.getClass().getResource("/").getPath();
		cfg.setDirectoryForTemplateLoading(new File(classpath + "templates"));

		//System.out.println(htmlTarget);
        //System.out.println(classpath + "templates");

		// 1. 获得现有的模板 ftl 文件
		Template template = cfg.getTemplate("stu.ftl", "utf-8");

		// 2. 获得动态数据
		String stranger = "FreemarkerController";
		model.addAttribute("there", stranger);
		makeModel(model);

		// 3. 融合动态数据和 ftl，生成html
		File tempDic = new File(htmlTarget);
		if (!tempDic.exists()) {
			tempDic.mkdirs();
		}

		Writer out = new FileWriter(htmlTarget + File.separator + "10010" + ".html");
		template.process(model, out);


		out.close();

		return "ok";
	}


	@GetMapping("/hello")
	public String hello(Model model) {
		// 输出字符串
		String stranger = "FreemarkerController";
		model.addAttribute("there", stranger);

		makeModel(model);

		// 返回的是freemarker模板所在的目录位置：/templates/；可以再yml中进行配置
		// 匹配 *.ftl；ftl为freemarker的文件后缀名
		return "stu";
	}

	private Model makeModel(Model model) {

		Stu stu = new Stu();
		stu.setUid("10010");
		stu.setUsername("dongruan");
		stu.setAmount(88.86f);
		stu.setAge(18);
		stu.setHaveChild(false);
		stu.setBirthday(new Date());

		Spouse spouse = new Spouse();
		spouse.setUsername("Lucy");
		spouse.setAge(25);

		stu.setSpouse(spouse);

		stu.setArticleList(getArticles());
		stu.setParents(getParents());

		model.addAttribute("stu", stu);

		return model;
	}

	private List<Article> getArticles() {
		Article article = new Article();
		article.setId("10001");
		article.setTitle("今天天气不错噢~");

		Article article2 = new Article();
		article2.setId("30002");
		article2.setTitle("今天打雷下雨~");

		Article article3 = new Article();
		article3.setId("30003");
		article3.setTitle("今天去野炊~");

		List<Article> list = new ArrayList<>();
		list.add(article);
		list.add(article2);
		list.add(article3);

		return list;
	}

	private Map<String, String> getParents() {
		Map<String, String> map = new HashMap<>();
		map.put("father", "LiLei");
		map.put("mather", "Hanmeimei");
		return map;
	}






}
