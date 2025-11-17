package com.ticketbotai.ai;

import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import java.io.InputStream;

import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

@Service
public class TextClassificationService {

    private Classifier classifier;
    private StringToWordVector filter;
    private Instances dataStructure; // Irá guardar a estrutura ANTES de filtrar
    private Attribute classAttribute;

    @PostConstruct
    public void init() {
        try {
            buildClassifier();
        } catch (Exception e) {
            // Se isto falhar, veremos no log de arranque
            System.err.println("!!!!!!!!!!!! ERRO AO CONSTRUIR CLASSIFICADOR !!!!!!!!!!!!");
            e.printStackTrace();
            System.err.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        }
    }

    public void buildClassifier() throws Exception {
        InputStream is = getClass().getClassLoader().getResourceAsStream("data/training-data.arff");
        if (is == null) {
            throw new Exception("Arquivo 'data/training-data.arff' não encontrado no classpath.");
        }

        DataSource source = new DataSource(is);
        Instances data = source.getDataSet();
        
        if (data.classIndex() == -1) {
            data.setClassIndex(data.numAttributes() - 1);
        }

        // --- CORREÇÃO DE LÓGICA DE BUILD ---
        
        // 1. Guarda a estrutura ORIGINAL (não filtrada)
        // Precisamos disto para criar novas instâncias no futuro.
        this.dataStructure = data.stringFreeStructure(); 
        this.classAttribute = data.attribute(data.classIndex());

        // 2. Configura o filtro
        this.filter = new StringToWordVector();
        filter.setInputFormat(data);

        // 3. Aplica o filtro aos dados de treino
        Instances filteredData = Filter.useFilter(data, filter);

        // 4. Constrói o classificador com os dados JÁ FILTRADOS
        this.classifier = new NaiveBayes();
        classifier.buildClassifier(filteredData);
    }

    public String classifyTicket(String text) {
        if (this.classifier == null) {
            // Se o buildClassifier() falhou no arranque
            return "Erro: Classificador não foi treinado.";
        }

        try {
            // --- CORREÇÃO DE LÓGICA DE CLASSIFICAÇÃO ---
            
            // 1. Cria um 'holder' baseado na estrutura ORIGINAL (com o atributo String)
            Instances testInstanceHolder = new Instances(this.dataStructure, 0);
            testInstanceHolder.setClass(this.classAttribute);

            // 2. Cria a nova instância (o novo ticket)
            DenseInstance newInstance = new DenseInstance(testInstanceHolder.numAttributes());
            newInstance.setDataset(testInstanceHolder);
            
            // 3. Define o valor do texto (o atributo String no índice 0)
            newInstance.setValue(0, text); 
            
            // 4. Adiciona a instância ao 'holder'
            testInstanceHolder.add(newInstance);

            // 5. APLICA O FILTRO (O passo que faltava)
            // Converte o texto da instância num vetor de palavras, tal como no treino
            Instances filteredInstance = Filter.useFilter(testInstanceHolder, this.filter);

            // 6. Classifica a instância (agora filtrada)
            double predictionIndex = classifier.classifyInstance(filteredInstance.firstInstance());
            
            // 7. Retorna o nome da classe
            return this.classAttribute.value((int) predictionIndex);

        } catch (Exception e) {
            e.printStackTrace();
            // Este erro agora significa uma falha na *predição*
            return "Erro durante a classificação"; 
        }
    }
}