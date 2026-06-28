package com.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Stream;

/// Uma aplicação desktop (usando a biblioteca [OpenJFX (JavaFX)](http://openjfx.io))
/// que desenha polígonos na tela e calcula o perímetro de cada um:
/// a soma da distância entre todos os seus pontos.
public class PoligonosApp extends Application {
    /**
     * Lista onde cada elemento representa os pontos que formam um polígono.
     * Cada elemento é então uma lista de pontos com coordenadas x,y.
     * Assim, cada polígono é formado por uma lista de pontos.
     */
    private final List<List<Point>> pontosPoligonos = List.of(
        // Quadrilátero (Quadrado)
        List.of(
            new Point( 50,  50),
            new Point(150,  50),
            new Point(150, 150),
            new Point( 50, 150)
        ),

        // Quadrilátero (Retângulo)
        List.of(
            new Point(200,  50),
            new Point(400,  50),
            new Point(400, 100),
            new Point(200, 100)
        ),

        // Triângulo
        List.of(
            new Point(300, 250),
            new Point(350, 150),
            new Point(400, 250)
        ),

        // Pentágono
        List.of(
            new Point(200, 250),
            new Point(250, 300),
            new Point(250, 350),
            new Point(150, 350),
            new Point(150, 300)
        ),

        // Hexágono
        List.of(
                new Point(320, 270),
                new Point(370, 320),
                new Point(370, 370),
                new Point(320, 420),
                new Point(270, 370),
                new Point(270, 320)
        )
    );

    /// Executa a aplicação.
    /// Esta é a nova forma de escrever o velho `public static void main(String args[])`
    /// quando não se tem parâmetros de linha de comando.
    void main() {
        launch();
    }

    /**
     * Inicia a apresentação da interface gráfica da aplicação.
     * @param mainStage janela inicial da aplicação
     */
    @Override
    public void start(final Stage mainStage) {
        final var root = new Pane();
        final var scene = new Scene(root, 800, 600);

        for (final var listaPontos : pontosPoligonos) {
            final var poligono = new Polygon();
            for (final Point point : listaPontos) {
                poligono.getPoints().addAll(point.x(), point.y());
            }

            poligono.setFill(Color.BLUE);
            poligono.setStroke(Color.BLACK);
            root.getChildren().add(poligono);
        }

        final List<String> perimetros = perimetros().stream().map(p -> String.format("%.1f", p)).toList();
        final var label1 = newLabel("Perímetro dos Polígonos: " + perimetros, 500);
        final var label2 = newLabel("Tipo dos Polígonos: " + tipoPoligonos(), 530);
        root.getChildren().addAll(label1, label2);

        mainStage.setTitle("Polígonos");
        mainStage.setScene(scene);
        mainStage.setAlwaysOnTop(true);
        mainStage.show();
    }

    private static Label newLabel(final String title, final int y) {
        final var label = new Label(title);
        label.setLayoutX(10);
        label.setLayoutY(y);
        return label;
    }

    /// Descobre o tipo de cada polígono a partir da quantidade de pontos que ele tem.
    /// Se um polígono representado por um elemento na lista [#pontosPoligonos] tiver 4 pontos,
    /// ele é um "Quadrilátero", se tiver 3 é um "Triângulo" e assim por diante.
    ///
    /// A implementação do método deve usar a operação [Stream#flatMap(Function)] que
    /// percorre os itens de [#pontosPoligonos] (cada item representando um polígono).
    /// O flatMap recebe então cada um destes itens, que é uma lista de pontos.
    /// A partir de tal lista, deve obter o total de pontos.
    /// Em uma operação posterior na cadeia de Stream, deve-se verificar
    /// qual o total de pontos de cada item que representa um polígono
    /// e devolver uma String indicando o tipo de polígono.
    ///
    /// @return uma lista de String indicando se o polígono é um "quadrilátero" (quadrado ou retângulo),
    /// "triângulo", "pentágono", "hexágono" ou apenas um "polígono" geral quando tiver mais de 6 lados.
    protected List<String> tipoPoligonos(){
        // TODO Apague esta linha e a próxima e implemente seu código
        return List.of();
    }

    /// Calcula o perímetro de cada polígono.
    /// O perímetro é a soma da distância entre cada [Point] (x,y) do [Polygon].
    /// Se você pensar em um polígono como um quadrado, o perímetro representa a distância que você percorreria
    /// se andasse ao redor da borda do quadrado, do ponto inicial até o último ponto.
    ///
    /// Este método é mais complexo. A implementação dele deve usar a operação [Stream#flatMap(Function)] que
    /// percorre os itens de [#pontosPoligonos] (cada item representando um polígono).
    /// O que queremos obter de cada item (lista de pontos) é a soma da distância entre cada ponto.
    ///
    /// O record [Point] (veja javadoc dele para mais detalhes)
    /// possui um construtor [Point#Point(Point,Point)] que recebe 2 pontos, cria um novo que contém:
    /// 1. as coordenadas do segundo ponto;
    /// 2. a distância entre os pontos no atributo [Point#distance] (acessado pelo método getter [Point#distance()]).
    /// Tal construtor já soma a distância entre p1 e p2 com a distância do p1 com o ponto anterior a ele.
    ///
    /// Assim, você precisaria percorrer todos os pontos de um polígono, pegar um par de pontos e passar
    /// para tal construtor. Pegar o terceiro ponto e o ponto criado na chamada do construtor anteriormente,
    /// e chamar o construtor novamente, passando estes 2 pontos, até que você chegue ao último ponto
    /// do polígono.
    /// Mas é exatamente isso que o reduce faz. Então para obter um ponto final contendo a soma da distância entre
    /// todos os pontos de um polígono, você deve usar o método reduce no parâmetro recebido no flatMap.
    ///
    /// No entanto, considere que temos um triângulo. Se utilizamos o método [Stream#reduce(BinaryOperator)],
    /// ele permitirá calcular a distância entre os pontos A → B e B → C somente.
    /// Mas para calcular o perímetro, precisamos fechar os pontos, obtendo também a distância entre C → A.
    /// Assim, podemos começar do A (1º ponto) e indicar que o ponto anterior é o C.
    /// Apesar de iniciar do A, estaríamos calculando as distâncias entre C → A, A → B e B → C, fechando
    /// todos os pontos. Com a versão do reduce indicada acima, não será possível fazer isso.
    /// Desta forma, a versão [Stream#reduce(Object,BinaryOperator)] deve ser usada no lugar.
    /// Leia o JavaDoc de tal método para mais detalhes.
    ///
    /// Após o flatMap, você vai ter um único ponto para cada polígono, que representa o último ponto encontrado
    /// e contém o perímetro do polígono, que pode ser acessado por [Point#distance()].
    /// Desta forma, basta retornar este resultado como uma nova lista de [Double].
    ///
    /// @return uma lista contendo o perímetro de cada polígono
    protected List<Double> perimetros(){
        // TODO Apague esta linha e a próxima e implemente seu código
        return List.of();
    }
}

