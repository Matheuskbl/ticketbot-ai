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
    private Instances dataStructure;
    private Attribute classAttribute;

    @PostConstruct
    public void init() {
        try {
            System.out.println("--- INICIANDO TREINAMENTO DA IA ---");
            buildClassifier();
            System.out.println("--- IA TREINADA COM SUCESSO ---");
        } catch (Exception e) {
            System.err.println("!!!!!!!!!!!! ERRO FATAL AO TREINAR IA !!!!!!!!!!!!");
            e.printStackTrace(); // ISSO VAI MOSTRAR O ERRO NO TERMINAL
        }
    }

    public void buildClassifier() throws Exception {
        // Tenta carregar o arquivo
        InputStream is = getClass().getClassLoader().getResourceAsStream("data/training-data.arff");
        if (is == null) {
            throw new Exception("ERRO DE CAMINHO: Arquivo 'data/training-data.arff' não encontrado em src/main/resources/data/");
        }

        DataSource source = new DataSource(is);
        Instances data = source.getDataSet();
        
        if (data.classIndex() == -1) {
            data.setClassIndex(data.numAttributes() - 1);
        }

        this.dataStructure = data.stringFreeStructure();
        this.classAttribute = data.attribute(data.classIndex());

        this.filter = new StringToWordVector();
        filter.setInputFormat(data);
        Instances filteredData = Filter.useFilter(data, filter);

        this.classifier = new NaiveBayes();
        classifier.buildClassifier(filteredData);
    }

    public String classifyTicket(String text) {
        if (this.classifier == null) {
            return "Erro: Classificador não foi treinado.";
        }

        try {
            Instances testInstanceHolder = new Instances(this.dataStructure, 0);
            testInstanceHolder.setClass(this.classAttribute);

            DenseInstance newInstance = new DenseInstance(testInstanceHolder.numAttributes());
            newInstance.setDataset(testInstanceHolder);
            newInstance.setValue(0, text); 
            
            testInstanceHolder.add(newInstance);

            Instances filteredInstance = Filter.useFilter(testInstanceHolder, this.filter);
            double predictionIndex = classifier.classifyInstance(filteredInstance.firstInstance());
            
            return this.classAttribute.value((int) predictionIndex);

        } catch (Exception e) {
            e.printStackTrace();
            return "Erro durante a classificação";
        }
    }
}