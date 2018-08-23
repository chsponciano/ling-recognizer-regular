package br.reconhecedor.vo1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import javafx.collections.transformation.SortedList;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Administrador
 */
public class Lexico {

    public static Lexico object;
    private List<String> palavras_reservadas;
    private List<String> caracteres_especiais;

    private Lexico() {
        this.palavras_reservadas  = new ArrayList<String>();
        this.caracteres_especiais = new ArrayList<String>();

        this.palavras_reservadas.add("abaaa");
        this.palavras_reservadas.add("acaaa");
        this.palavras_reservadas.add("aaaba");
        this.palavras_reservadas.add("aaaca");

        this.caracteres_especiais.add(";");
        this.caracteres_especiais.add(",");
        this.caracteres_especiais.add(".");
    }

    public static Lexico getInstance() {
        if (object == null) {
            object = new Lexico();
        }
        return object;
    }

    public void reconhecimento(JTextArea entrada, JTable saida) {
        List lista = montaListaDePalvras(entrada.getText());
        Iterator iterator = lista.iterator();

        DefaultTableModel dtm = (DefaultTableModel) saida.getModel();
        dtm.setRowCount(0);
        
        while (iterator.hasNext()) {
            dtm.addRow(valida_palavra(Integer.parseInt(String.valueOf(iterator.next())), String.valueOf(iterator.next())));
        }
    }

    public Object[] valida_palavra(int linha, String palavra) {
        if (this.caracteres_especiais.contains(palavra)) {
            return new Object[]{linha, "Simbolo Especial", palavra, "q0, qEspecial"};
        }
        if (this.palavras_reservadas.contains(palavra)) {
            return new Object[]{linha, "Palavra Reservada", palavra, Estado.getInstance().validar_percuso(palavra)};
        }
        if ((palavra.charAt(0) != 'a') && (palavra.charAt(0) != 'b') && (palavra.charAt(0) != 'c')) {
            return new Object[]{linha, "Erro: simbolo(s) inválido(s)", palavra, "q0, qErro"};
        }

        String reconhecimento = Estado.getInstance().validar_percuso(palavra);
        return new Object[]{linha, (!Estado.isErro) ? "Palavra válida" : "Erro: palavra inválida", palavra, reconhecimento};
    }

    private List montaListaDePalvras(String conteudo) {
        List<Object> palavras = new ArrayList<>();
        int linha = 1;
        String aux = "";

        for (char c : conteudo.toCharArray()) {
            if (this.caracteres_especiais.contains(String.valueOf(c))) {//Caracter Especial
                if (aux.length() > 0) {
                    palavras.add(linha);
                    palavras.add(aux);
                    aux = "";
                }
                palavras.add(linha);
                palavras.add(c);
            } else if (c == '\n') {//Pula linha
                if (aux.length() > 0) {
                    palavras.add(linha);
                    palavras.add(aux);
                    aux = "";
                }
                linha++;
            } else if (Character.getType(c) == 15 || Character.getType(c) == 12) {//Tabulacao ou Espaco em branco
                if (aux.length() > 0) {
                    palavras.add(linha);
                    palavras.add(aux);
                    aux = "";
                }
            } else {
                aux += c;
            }
        }

        if (aux.length() > 0) {
            palavras.add(linha);
            palavras.add(aux);
        }

        return palavras;
    }
}