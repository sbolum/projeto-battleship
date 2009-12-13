package view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 *
 * @author Carlos Henrique Silva Galdino
 * @author Raphael Pereira de Faria
 *
 */
public class Janela extends JFrame {
    private JPanel painelPrincipal;
    private JPanel painelDesenho;
    private String nomeRadios[] = { "Fácil", "Médio", "Difícil" };
    private ButtonGroup grupo;
    private JRadioButtonMenuItem radioButtons[];
    private int dificuldade;
    //private Grid grid;
    private JMenu botaoNovoJogo;
    private Controle controle;
    private PainelInfo painelInfo;
    private PainelLateral painelLateral;
    private JButton botoes[][];

    /**
     * Construtor do Frame
     * @param titulo título do frame
     * @param cont1  objeto da classe controle
     */
    public Janela(String titulo) {//, Controle cont1) {
        super(titulo);
        //this.controle = cont1;
        //this.grid = grid;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        //setResizable(false);
        setLocationRelativeTo(null);
        painelPrincipal = new JPanel(new BorderLayout());
        painelPrincipal.add(criaMenu(), BorderLayout.NORTH);
        painelLateral = new PainelLateral();
        painelDesenho = painelTeste();
        painelDesenho.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                try {
                    // Captura a coordenada do clique
                    Point ponto = new Point(interacaoMouse(e.getX(), e.getY()));

                    // Remove o ponto do caminho se for o caso
                    //controle.inserirNavio(navio, ponto);
                    repaint();
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(null, exception.getMessage(), "Posição inválida", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        /*
        painelDesenho.addMouseMotionListener(new MouseMotionListener() {

            public void mouseMoved(MouseEvent event) {
            }

            public void mouseDragged(MouseEvent e) {
            }
        });*/
        painelPrincipal.add(painelDesenho, BorderLayout.CENTER);
        painelDesenho.add(painelInfo = new PainelInfo(), BorderLayout.SOUTH);
        getContentPane().add(painelPrincipal);
        //pack();
        setPreferredSize(new Dimension(700, 500));
        setVisible(true);
    }

    /**
     * Método que constrói o menu
     * @return barra de menu
     */
    private JMenuBar criaMenu() {
        JMenuBar barraMenu = new JMenuBar();
        JMenu principal = new JMenu("Principal");
        botaoNovoJogo = new JMenu("Novo Jogo");
        JMenuItem contraComputador = new JMenuItem("Jogador vs. Computador");
        JMenuItem contraJogador = new JMenuItem("Jogador vs. Jogador");
        botaoNovoJogo.add(contraComputador);
        botaoNovoJogo.add(contraJogador);

        contraComputador.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                //TODO adicionar o comportamento de novo jogo
                //painelPrincipal.remove(painelLateral);
                
                painelPrincipal.add(painelLateral, BorderLayout.EAST);
                painelLateral.atualizarTorpedos(30);
                System.out.println("entrei");
                pack();
                repaint();
            }
        });

        contraJogador.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                //TODO adicionar o comportamento de novo jogo
                //painelPrincipal.remove(painelLateral);
                //painelLateral = new PainelLateral();
                painelPrincipal.add(painelLateral, BorderLayout.EAST);
                System.out.println("entrei");
                painelLateral.atualizarCombo();
                pack();
                repaint();
            }
        });

        JMenuItem botaoSair = new JMenuItem("Sair");
        botaoSair.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
            
        });
        principal.add(botaoNovoJogo);
        principal.add(botaoSair);

        JMenu terrenos = new JMenu("Dificuldade");
        grupo = new ButtonGroup();
        radioButtons = new JRadioButtonMenuItem[nomeRadios.length];
        Gerenciador gerenciador = new Gerenciador();
        for (int i = 0; i < nomeRadios.length; i++) {
            radioButtons[i] = new JRadioButtonMenuItem(nomeRadios[i]);
            terrenos.add(radioButtons[i]);
            grupo.add(radioButtons[i]);
            radioButtons[i].addItemListener(gerenciador);
        }
        radioButtons[1].setSelected(true);
        barraMenu.add(principal);
        barraMenu.add(terrenos);

        return barraMenu;
    }

    /**
     * Método que captura a coordenada relativa ao clique no grid
     * @param x - valor do clique no eixo x
     * @param y - valor do clique no eixo y
     * @return ponto contendo a coordenada no grid
     */
    private Point interacaoMouse(int x, int y) {
        x /= 44;
        y /= 33;
        x--;
        y--;
        System.out.println("(" + x + ", " + y + ")");
        return new Point(x, y);
    }

    /**
     * Classe que gerencia a mudança de estados dos radioButtons
     */
    private class Gerenciador implements ItemListener {
        public void itemStateChanged(ItemEvent e) {
            for(int i = 0; i < radioButtons.length; i++) {
                if(radioButtons[i].isSelected())
                    dificuldade = i;
            }
        }
    }

    private JPanel painelTeste() {
        JPanel painelPrin = new JPanel(new BorderLayout());
        JPanel painel = new JPanel(new GridLayout(11, 11));
        String letras[] = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J" };
        botoes = new JButton[10][10];
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                if (i > 0 && j > 0) {
                    JButton botao = new JButton();
                    botao.setBackground(Color.CYAN);
                    botoes[i-1][j-1] = botao;
                    botao.addMouseListener(new MouseAdapter() {

                        @Override
                        public void mouseClicked(MouseEvent e) {
                            JButton botao = (JButton)e.getSource();
                            Point ponto = interacaoMouse(botao.getLocation().x, botao.getLocation().y);
                            String selecionado = painelLateral.opcaoSelecionada();
                            System.out.println(selecionado);
                             try {
                                if (controle.isJogoComecou()) {
                                     if (e.getButton() == MouseEvent.BUTTON1) {
                                        if (!selecionado.contains("Mina"))
                                            controle.inserirNavio(painelLateral.opcaoSelecionada(), ponto, false);
                                        else
                                            controle.inserirMina(ponto);
                                    }
                                    else if (e.getButton() == MouseEvent.BUTTON2) {
                                        if (!selecionado.contains("Mina"))
                                            controle.inserirNavio(painelLateral.opcaoSelecionada(), ponto, true);
                                        else
                                            controle.inserirMina(ponto);
                                    }
                                }
                            } catch (Exception exception) {
                                JOptionPane.showMessageDialog(null, exception.getMessage(), "Informação", JOptionPane.WARNING_MESSAGE);
                            }
                        }

                    });
                    painel.add(botao);
                }
                else {
                    JTextField field = new JTextField();
                    if (i == 0) {
                        if (j == 0)
                            field.setText("");
                        else
                            field.setText(letras[j-1]);
                    }
                    else if (j == 0) {
                        field = new JTextField(Integer.toString(i));
                    }
                    field.setEditable(false);
                    field.setHorizontalAlignment(JTextField.CENTER);
                    painel.add(field);
                }
            }
        }
        painelPrin.add(painel, BorderLayout.CENTER);
        return painelPrin;
    }


    /**
     * Classe que representa o painel que exibe as informações a respeito
     * do caminho encontrado.
     */
    private class PainelInfo extends JPanel {
        private JTextArea textArea;
        private JScrollPane scrollPane;

        public PainelInfo() {
            textArea = new JTextArea(4, 42);
            scrollPane = new JScrollPane(textArea);
            textArea.setEditable(false);
            this.add(scrollPane, BorderLayout.WEST);
        }

        //TODO implementar método das mensagens do board
        /*
        public void atualizarInformacoes(String info) {
            informacoes.setText(informacoes + info);
        }*/
    }

    private class PainelLateral extends JPanel {
        private JComboBox comboBox;
        private String barcos[] = { "Battleship", "Cruiser", "Submarine", "Destroyer", "Patrol Boat", "Mina 1", "Mina 2" };
        private JLabel labelTorpedos;
        private JPanel painelCombo;

        public PainelLateral() {
            this.setLayout(new BorderLayout());
            painelCombo = new JPanel();
            comboBox = new JComboBox(barcos);
            JButton botaoPronto = new JButton("Pronto");
            botaoPronto.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    //controle.iniciarJogo();
                }
            });
            botaoPronto.setEnabled(false);
            System.out.println("entrei no painellatera");
            labelTorpedos = new JLabel("Total de torpedos restantes = ");
            painelCombo.add(comboBox);
            painelCombo.add(botaoPronto);
            this.add(painelCombo, BorderLayout.NORTH);
            this.add(labelTorpedos, BorderLayout.CENTER);
            painelCombo.setVisible(false);
            labelTorpedos.setVisible(false);
        }

        public void atualizarTorpedos(int valor) {
            labelTorpedos.setText("Total de torpedos restantes = " + valor);
            painelCombo.setVisible(false);
            labelTorpedos.setVisible(true);
            this.repaint();
        }

        public void atualizarCombo() {
            painelCombo.setVisible(true);
            labelTorpedos.setVisible(false);
            this.repaint();
        }

        public String opcaoSelecionada() {
            return (String)comboBox.getSelectedItem();
        }

    }

}