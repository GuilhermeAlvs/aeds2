import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.*;

 class Filme{
    private String nome;
    private String titulo;
    private Date data;
    private int duracao;
    private String genero;
    private String idioma;
    private String situacao;
    private float orcamento;
    private String chave[];
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    // Declaracao Filmes

    Filme(String nome, String titulo, Date data, int duracao, String genero, String idioma, String situacao,
            float orcamento){
        this.nome = nome;
        this.titulo = titulo;
        this.data = data;
        this.duracao = duracao;
        this.genero = genero;
        this.idioma = idioma;
        this.situacao = situacao;
        this.orcamento = orcamento;
    }

    // Get-Set Nome
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return this.nome;
    }

    // Get-Set Titulo
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTitulo() {
        return this.titulo;
    }

    // Get-Set data
    public void setData(Date data) {
        this.data = data;
    }

    public Date getData() {
        return this.data;
    }

    // Get-Set Duracao
    public void setDuracao(int duracao) {
        this.duracao = duracao;
    }

    public Integer setDuracao() {
        return this.duracao;
    }

    // Get-Set Genero
    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getGenero() {
        return this.genero;
    }

    // Get-Set Idioma
    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public String getIdioma() {
        return this.idioma;
    }

    // Get-Set Situacao
    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getSituacao() {
        return this.situacao;
    }

    // Get-Set orcamento
    public void setOrcamento(float orcamento) {
        this.orcamento = orcamento;
    }

    public float setOrcamento() {
        return this.orcamento;
    }

    // Get-Set Palavra Chave
    public void setChave(String chave[]) {
        this.chave = chave;
    }

    public String[] getChave() {
        return chave;
    }

    // Tirando as tags HTML
    String removeTags(String line) {
        String newline = "";
        int i = 0;
        while (i < line.length()) {
            if (line.charAt(i) == '<') {
                i++;
                while (line.charAt(i) != '>')
                    i++;
            } else {
                newline += line.charAt(i);
            }
            i++;
        }

        newline = newline.replace("&nbsp;", " ");
        newline = newline.replace("Título original", " ");
        newline = newline.replace("Idioma original", " ");
        newline = newline.replace("Situação", " ");
        newline = newline.replace("(BR)", " ");
        newline = newline.replace("Orçamento", " ");
        newline = newline.replace("$", " ");
        newline = newline.replace(",", "");
        return newline;
    }

    //mudando pra min
    public int duracaoToInt(String s){
        String aux[];
        String minuto = "";
        int minutosTotais;

        if (s.contains("h")){
            aux = s.split("h");
            minuto = aux[1].substring(0, aux[1].length() - 1).trim();
            minutosTotais = (Integer.parseInt(aux[0].trim()) * 60) + (Integer.parseInt(minuto));
        } else {
            minuto += s.trim();
            minuto = minuto.replace("m", " ");

            minutosTotais = Integer.parseInt(minuto.trim());
        }

        return minutosTotais;
    }

    //Identifica Parentese
    public String parentese(String line) {
        String novo = "";
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) != '(') {
                novo += line.charAt(i);
            } else {
                return novo;
            }
        }
        return novo;

    }

    // Leitor
    public void ler(String nomeArquivo) throws Exception {
        InputStreamReader fr = new FileReader(nomeArquivo);
        BufferedReader br = new BufferedReader(fr);
        String linha = br.readLine();

        //Ler Nome
        while (!(linha = br.readLine()).contains("<title>"))
            ;
        this.nome = parentese(removeTags(linha).trim());

        //Ler Data

        while (!(linha = br.readLine()).contains("span class=\"release\""))
            ;
        linha = br.readLine();
        this.data = sdf.parse(parentese(removeTags(linha).trim()));

        //Ler Genero 
        while(!(linha=br.readLine()).contains("genres"));
        br.readLine();
        linha = br.readLine();
        linha = removeTags(linha.trim());
        String auxi[];
        auxi = linha.split(" ");
        this.genero = palavraToStringNoSpace(auxi);

        //Ler Duracao
        while (!(linha = br.readLine()).contains("runtime"))
            ;
        br.readLine();
        linha = br.readLine();
        this.duracao = duracaoToInt(removeTags(linha).trim());

        //Ler Titulo
        while (!(linha = br.readLine()).contains("<section class=\"facts left_column\">"))
            ;
        while (!(linha = br.readLine()).contains("<strong><bdi>Situação</bdi></strong>")) {
            if ((linha = br.readLine()).contains("Título original")) {
                this.titulo = removeTags(linha).trim();
            }
        }

        if (titulo == "") {
            this.titulo = nome;
        }

        this.situacao = removeTags(linha).trim();

        //Ler Idioma
        while (!(linha = br.readLine()).contains("Idioma original"))
            ;
        this.idioma = removeTags(linha).trim();

        //Ler Orcamento
        while (!(linha = br.readLine()).contains("Orçamento"))
            ;
        if (linha.contains("<p><strong><bdi>Orçamento</bdi></strong> -</p>")) {
            linha = removeTags(linha).trim();
            this.orcamento = 0;
        } else {
            this.orcamento = Float.parseFloat(removeTags(linha).trim());
        }

        // Lendo as palavras-chave
        // e verificando se nao tem nenhuma
        while (!(linha = br.readLine()).contains("Palavras-chave"))
            ;
        linha = "";
        br.readLine();
        linha = br.readLine();
        if (linha.contains("Nenhuma palavra-chave foi adicionada.")) {
            this.chave = removeTags(linha).trim().split("--");
        } else {
            while (!(linha += br.readLine().trim() + "-").contains("</ul>"))
                ;
            this.chave = removeTags(linha).trim().split("--");
        }

        br.close();
    }

    //Chave->string
    public String palavraToString(String s[]){
        String aux = "";
        int i = 0;
        if(s[0].contains("Nenhuma chave")){
            aux = "";
        }else{
            for(; i < s.length - 2; i++){
                aux+=s[i];
                aux+=  ", ";
            }
            aux+=s[i];
        }
        return aux.replace("-", "");
    }

    public String palavraToStringNoSpace(String s[]){
        String aux = "";
        int i = 0;
        if(s[0].contains("Nenhuma palavra-chave foi adicionada.")){
            aux = "";
        }else{
            for(; i < s.length - 1; i++){
                aux+=s[i];
                aux+=  ",";
            }
            aux+=s[i];
        }
        return aux.replace("-", "");
    }

    // clone
    public Filme clone() {
        Filme resp = new Filme(genero, genero, data, duracao, genero, genero, genero, orcamento);
        resp.nome = this.nome;
        resp.titulo = this.titulo;
        resp.data = this.data;
        resp.duracao = this.duracao;
        resp.genero = this.genero;
        resp.idioma = this.idioma;
        resp.situacao = this.situacao;
        resp.orcamento = this.orcamento;
        return resp;
    }

    // imprimir
    public void Imprimir() {
        System.out.println(nome + " " + titulo + " " + data + " " + duracao + " " + genero + " " + idioma + " "
                + situacao + " " + orcamento + " ");
    }

}

/*public class TP02Q01Class{

    public static boolean isFim(String s){
        return (s.length() == 3 && s.charAt(0) == 'F' && s.charAt(1) == 'I' && s.charAt(2) == 'M');
    }

    public static void main(String[] args) throws Exception{
        String[] input = new String[1000];
        int numInput = 0;
        MyIO.setCharset("UTF-8");
        do {
            input[numInput] = MyIO.readLine();
        } while (isFim(input[numInput++]) == false);
        numInput--;// Desconsiderar a palavra FIM

        Filme[] filmes = new Filme[numInput];

        for (int i = 0; i < numInput; i++){
            filmes[i] = new Filme (null, null, null, i, null, null, null, i);
            filmes[i].ler("/tmp/filmes/" + input[i]);
            filmes[i].Imprimir();

        }
    }
}
*/