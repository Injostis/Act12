/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.ui.Model;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author rod06
 */
@Controller
public class TriangleController {
    
    private JdbcTemplate jdbcTemplate;
    
    @Autowired
    public TriangleController (JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public String index(Model model){
        model.addAttribute("triangulo", new Triangle(0, 0, 0, 0));
        return "index";
    }
    
    @RequestMapping(value = "/calcular", method = RequestMethod.POST)
    public String calcular(@ModelAttribute Triangle triangle, Model model){
        int base = triangle.getBase();
        int altura = triangle.getAltura();
        int perimetro = base * 3;
        int area = (base * altura) / 2;
        jdbcTemplate.update("INSERT INTO resultados (base, altura, perimetro, area) VALUES (?, ?, ?, ?)",
                base, altura, perimetro, area);
        return "redirect:/resultados";
    }
    
    @RequestMapping(value = "/resultados", method = RequestMethod.GET)
    public String resultados(Model model){
        List<Triangle> triangle = jdbcTemplate.query(
                "SELECT * FROM resultados",
                new RowMapper<Triangle>() {
                    @Override
                    public Triangle mapRow(ResultSet rs, int rowNum) throws SQLException{
                        return new Triangle(
                        rs.getInt("base"),
                        rs.getInt("altura"),
                        rs.getInt("perimetro"),
                        rs.getInt("area"));
                    }
                });
        model.addAttribute("triangle", triangle);
        return "resultados";
    }
}
