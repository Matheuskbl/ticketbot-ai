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
    private Attribute textAttribute;
    private Attribute classAttribute;

    @PostConstruct
    public void init() {
        try {
            buildClassifier();
        } catch (Exception e) {
            e.printStackTrace();
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

        this.filter = new StringToWordVector();
        filter.setInputFormat(data);
        Instances filteredData = Filter.useFilter(data, filter);

        this.classifier = new NaiveBayes();
        classifier.buildClassifier(filteredData);

        this.dataStructure = filteredData.stringFreeStructure();
        this.textAttribute = data.attribute(0);
        this.classAttribute = data.attribute(data.classIndex());
    }

    public String classifyTicket(String text) {
        if (this.classifier == null) {
            return "Erro: Classificador não foi treinado.";
        }

        try {
            Instances testInstanceHolder = new Instances(this.dataStructure, 0);
            
            DenseInstance newInstance = new DenseInstance(2);
            newInstance.setDataset(testInstanceHolder);
            
            newInstance.setValue(this.textAttribute, text);
            
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