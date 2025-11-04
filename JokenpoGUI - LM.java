import javax.swing.*;
import java.awt.*;
import java.util.Random;

/**
 * Sistema de Jokenpô
 * Autor: Caio Silva Marques, Luan Victor Carvalho, Francisco Isaac
 */
public class JokenpoGUI extends JFrame {

    private final JLabel lblComputador;
    private final JLabel lblResultado;
    private final JLabel lblPlacar;
    private final JLabel lblModo;
    private final JLabel lblRodada;

    private final JButton btnPedra, btnPapel, btnTesoura;
    private final JButton btnFacil, btnMedio, btnDificil;
    private final JButton btnIniciar, btnEncerrar;

    private int pontosJogador = 0;
    private int pontosComputador = 0;
    private int rodadaAtual = 0;
    private final int TOTAL_RODADAS = 5;
    private String modoDificuldade = "Médio";
    private boolean jogoAtivo = false;

    private final Random random = new Random();

    public JokenpoGUI() {
        setTitle("Jokenpô - Pedra, Papel e Tesoura");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 550);
        setLocationRelativeTo(null);

        JPanel painelPrincipal = new PainelComFundo();
        painelPrincipal.setLayout(new BorderLayout(10, 10));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // ----- Título
        JLabel titulo = new JLabel("🎮 JOKENPÔ - Desafio Contra O PC!", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 28));
        titulo.setForeground(Color.WHITE);
        painelPrincipal.add(titulo, BorderLayout.NORTH);

        // ----- Painel central
        JPanel painelCentral = new JPanel(new GridBagLayout());
        painelCentral.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.CENTER;

        lblModo = new JLabel("Modo atual: " + modoDificuldade, SwingConstants.CENTER);
        lblModo.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblModo.setForeground(Color.WHITE);
        painelCentral.add(lblModo, gbc);

        gbc.gridy++;
        lblRodada = new JLabel("Rodada: - / " + TOTAL_RODADAS, SwingConstants.CENTER);
        lblRodada.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblRodada.setForeground(Color.WHITE);
        painelCentral.add(lblRodada, gbc);

        gbc.gridy++;
        lblComputador = new JLabel("Computador ainda não jogou.", SwingConstants.CENTER);
        lblComputador.setFont(new Font("SansSerif", Font.PLAIN, 18));
        lblComputador.setForeground(Color.WHITE);
        painelCentral.add(lblComputador, gbc);

        gbc.gridy++;
        lblResultado = new JLabel("Clique em INICIAR para começar!", SwingConstants.CENTER);
        lblResultado.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblResultado.setForeground(Color.WHITE);
        painelCentral.add(lblResultado, gbc);

        gbc.gridy++;
        lblPlacar = new JLabel("Placar → Silvio: 0 | PC: 0", SwingConstants.CENTER);
        lblPlacar.setFont(new Font("SansSerif", Font.PLAIN, 18));
        lblPlacar.setForeground(Color.WHITE);
        painelCentral.add(lblPlacar, gbc);

        // ----- Painel de botões
        gbc.gridy++;
        JPanel painelBotoes = new JPanel(new GridLayout(3, 1, 12, 12));
        painelBotoes.setOpaque(false);

        // Linha 1 - Botões de controle
        JPanel painelControle = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        painelControle.setOpaque(false);
        btnIniciar = criaBotaoModo("INICIAR JOGO", new Color(0, 120, 255));
        btnEncerrar = criaBotaoModo("ENCERRAR JOGO", new Color(180, 30, 30));
        painelControle.add(btnIniciar);
        painelControle.add(btnEncerrar);

        // Linha 2 - Botões de jogadas
        JPanel botoesJogo = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        botoesJogo.setOpaque(false);
        btnPedra = criaBotaoJogo("✊ PEDRA", new Color(255, 240, 230));
        btnPapel = criaBotaoJogo("✋ PAPEL", new Color(255, 240, 230));
        btnTesoura = criaBotaoJogo("✌️ TESOURA", new Color(255, 240, 230));
        botoesJogo.add(btnPedra);
        botoesJogo.add(btnPapel);
        botoesJogo.add(btnTesoura);

        // Linha 3 - Botões de dificuldade
        JPanel botoesModo = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        botoesModo.setOpaque(false);
        btnFacil = criaBotaoModo("FÁCIL", new Color(0, 150, 0));
        btnMedio = criaBotaoModo("MÉDIO", new Color(210, 160, 0));
        btnDificil = criaBotaoModo("Tenta A Sorte", new Color(170, 20, 20));
        botoesModo.add(btnFacil);
        botoesModo.add(btnMedio);
        botoesModo.add(btnDificil);

        painelBotoes.add(painelControle);
        painelBotoes.add(botoesJogo);
        painelBotoes.add(botoesModo);
        painelCentral.add(painelBotoes, gbc);
        painelPrincipal.add(painelCentral, BorderLayout.CENTER);
        add(painelPrincipal);

        // Ações dos botões
        btnIniciar.addActionListener(e -> iniciarJogo());
        btnEncerrar.addActionListener(e -> encerrarJogo());

        btnPedra.addActionListener(e -> jogar("Pedra"));
        btnPapel.addActionListener(e -> jogar("Papel"));
        btnTesoura.addActionListener(e -> jogar("Tesoura"));

        btnFacil.addActionListener(e -> mudarModo("Fácil"));
        btnMedio.addActionListener(e -> mudarModo("Médio"));
        btnDificil.addActionListener(e -> mudarModo("Impossível"));

        // Inicialmente desativa jogadas
        habilitarJogadas(false);
    }

    private JButton criaBotaoJogo(String texto, Color corFundo) {
        JButton botao = new JButton(texto);
        botao.setFont(new Font("SansSerif", Font.BOLD, 18));
        botao.setBackground(corFundo);
        botao.setFocusPainted(false);
        botao.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        botao.setPreferredSize(new Dimension(160, 60));
        return botao;
    }

    private JButton criaBotaoModo(String texto, Color cor) {
        JButton botao = new JButton(texto);
        botao.setFont(new Font("SansSerif", Font.BOLD, 16));
        botao.setForeground(Color.WHITE);
        botao.setBackground(cor);
        botao.setFocusPainted(false);
        botao.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        botao.setPreferredSize(new Dimension(160, 40));
        return botao;
    }

    private void iniciarJogo() {
        pontosJogador = 0;
        pontosComputador = 0;
        rodadaAtual = 0;
        jogoAtivo = true;

        lblResultado.setText("O Jogo começou! Faça sua jogada.");
        lblResultado.setForeground(Color.WHITE);
        lblPlacar.setText("Placar → Silvio: 0 | PC: 0");
        lblRodada.setText("Rodada: 1 / " + TOTAL_RODADAS);
        habilitarJogadas(true);
    }

    private void encerrarJogo() {
        if (!jogoAtivo) return;
        jogoAtivo = false;
        habilitarJogadas(false);
        String vencedor = calculaVencedorFinal();
        lblResultado.setText("Jogo encerrado. " + vencedor);
        lblResultado.setForeground(Color.YELLOW);
    }

    private void jogar(String jogadaJogador) {
        if (!jogoAtivo) {
            lblResultado.setText("Clique em INICIAR para começar!");
            return;
        }

        rodadaAtual++;
        lblRodada.setText("Rodada: " + rodadaAtual + " / " + TOTAL_RODADAS);

        String jogadaComputador = geraJogadaComputador(jogadaJogador);
        lblComputador.setText("PC jogou: " + jogadaComputador);

        String resultado = determinaResultado(jogadaJogador, jogadaComputador);

        if ("Vitória".equals(resultado)) {
            pontosJogador++;
            lblResultado.setForeground(new Color(0, 200, 0));
        } else if ("Derrota".equals(resultado)) {
            pontosComputador++;
            lblResultado.setForeground(new Color(200, 0, 0));
        } else {
            lblResultado.setForeground(Color.LIGHT_GRAY);
        }

        lblResultado.setText("Resultado: " + resultado);
        lblPlacar.setText("Placar → Silvio: " + pontosJogador + " | PC: " + pontosComputador);

        if (rodadaAtual >= TOTAL_RODADAS) {
            encerrarJogo();
        }
    }

    private void habilitarJogadas(boolean ativo) {
        btnPedra.setEnabled(ativo);
        btnPapel.setEnabled(ativo);
        btnTesoura.setEnabled(ativo);
        btnEncerrar.setEnabled(ativo);
    }

    private String geraJogadaComputador(String jogadaJogador) {
        String[] opcoes = {"Pedra", "Papel", "Tesoura"};
        int escolha = random.nextInt(3);

        if ("Impossível".equals(modoDificuldade)) {
            if ("Pedra".equals(jogadaJogador)) return "Papel";
            if ("Papel".equals(jogadaJogador)) return "Tesoura";
            if ("Tesoura".equals(jogadaJogador)) return "Pedra";
        } else if ("Médio".equals(modoDificuldade)) {
            int chance = random.nextInt(100);
            if (chance < 60) return jogadaJogador; // empate
        } else if ("Fácil".equals(modoDificuldade)) {
            int chance = random.nextInt(100);
            if (chance < 70) {
                if ("Pedra".equals(jogadaJogador)) return "Tesoura";
                if ("Papel".equals(jogadaJogador)) return "Pedra";
                if ("Tesoura".equals(jogadaJogador)) return "Papel";
            }
        }
        return opcoes[escolha];
    }

    private String determinaResultado(String jogador, String computador) {
        if (jogador.equals(computador)) return "Empate";
        if ("Pedra".equals(jogador))
            return "Tesoura".equals(computador) ? "Vitória" : "Derrota";
        if ("Papel".equals(jogador))
            return "Pedra".equals(computador) ? "Vitória" : "Derrota";
        if ("Tesoura".equals(jogador))
            return "Papel".equals(computador) ? "Vitória" : "Derrota";
        return "Empate";
    }

    private String calculaVencedorFinal() {
        if (pontosJogador > pontosComputador)
            return "É isso ai você Ganhou!";
        else if (pontosComputador > pontosJogador)
            return "O computador te amassou!";
        else
            return "A partida terminou empatada!";
    }

    private void mudarModo(String novoModo) {
        modoDificuldade = novoModo;
        lblModo.setText("Modo atual: " + novoModo);
    }

    // ----- Plano de fundo visual
    static class PainelComFundo extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Color laranjaSuave = new Color(255, 190, 110);
            g.setColor(laranjaSuave);
            g.fillRect(0, 0, getWidth(), getHeight());

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f));
            g2.setFont(new Font("SansSerif", Font.BOLD, 100));
            g2.setColor(Color.WHITE);

            String[] simbolos = {"✊", "✋", "✌"};
            int step = 200;
            for (int y = -40; y < getHeight() + 40; y += step) {
                for (int x = -40; x < getWidth() + 40; x += step) {
                    String s = simbolos[Math.abs((x + y) / step) % 3];
                    g2.drawString(s, x + 20, y + 100);
                }
            }
            g2.dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JokenpoGUI jogo = new JokenpoGUI();
            jogo.setVisible(true);
        });
    }
}
