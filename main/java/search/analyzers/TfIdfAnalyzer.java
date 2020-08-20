/**
 * Name: Chris Forbes
 * File: TfIdfAnalyzer.java
 * Date: 11/22/2017
 */
package search.analyzers;

import datastructures.concrete.KVPair;
import datastructures.concrete.dictionaries.ArrayDictionary;
import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IList;
import datastructures.interfaces.ISet;
import search.models.Webpage;

import java.net.URI;

/**
 * This class is responsible for computing how "relevant" any given document is
 * to a given search query.
 *
 * See the spec for more details.
 */
public class TfIdfAnalyzer {
    // This field contains the IDF score for every single word in all
    // the documents.
    private IDictionary<String, Double> idfScores;

    // This field contains the TF-IDF vector for each webpage you were given
    // in the constructor.
    private IDictionary<URI, IDictionary<String, Double>> documentTfIdfVectors;
    
    // This field contains the Euclidean norm value of all TF-IDF vectors for 
    // each webpage you were given in the constructor.
    private IDictionary<URI, Double> documentTfIdfNormVectors;

    public TfIdfAnalyzer(ISet<Webpage> webpages) {
        this.idfScores = this.computeIdfScores(webpages);
        this.documentTfIdfVectors = this.computeAllDocumentTfIdfVectors(webpages);
        this.documentTfIdfNormVectors = this.computeDocumentNormVector();
    }

    /**
     * Returns the TF-IDF vectors of all webpages
     */
    public IDictionary<URI, IDictionary<String, Double>> getDocumentTfIdfVectors() {
        return this.documentTfIdfVectors;
    }

    /**
     * Returns a dictionary mapping every single unique word found
     * in any documents to their IDF score.
     */
    private IDictionary<String, Double> computeIdfScores(ISet<Webpage> pages) {
    	IDictionary<String, KVPair<URI, Integer>> wordFrequency = 
    			new ChainedHashDictionary<String, KVPair<URI, Integer>>();
    	IDictionary<String, Double> computedIdfScores = new ChainedHashDictionary<String, Double>();
    	
    	// calculate word frequency
    	for (Webpage webpage : pages) {
    		URI uri = webpage.getUri();
    		IList<String> words = webpage.getWords();
    		for (String word : words) {
    			if (!wordFrequency.containsKey(word)) {
    				wordFrequency.put(word, new KVPair<URI, Integer>(uri, 1));
    			} else {
    				KVPair<URI, Integer> wordDocToFreq = wordFrequency.get(word);
        			int freq = wordDocToFreq.getValue();
    				if (!wordDocToFreq.getKey().equals(uri)) {
    					KVPair<URI, Integer> updatedFreq = new KVPair<URI, Integer>(uri, freq + 1);
    					wordFrequency.put(word, updatedFreq);
        			} 
    			}
    		}
    	}
    	
    	// calculate IDF score based on calculated word frequencies
    	for (KVPair<String, KVPair<URI, Integer>> pair : wordFrequency) {
    		String word = pair.getKey();
    		KVPair<URI, Integer> wordDocToFreq = pair.getValue();
    		double freq = (double) pages.size() / (double) wordDocToFreq.getValue();
    		double idfScore = Math.log(freq);
    		computedIdfScores.put(word, idfScore);
    	}
    	return computedIdfScores;
    }

    /**
     * Returns a dictionary mapping every unique word found in the given list
     * to their term frequency (TF) score.
     *
     * We are treating the list of words as if it were a document.
     */
    private IDictionary<String, Double> computeTfScores(IList<String> words) {
    	IDictionary<String, Double> computedTfScores = new ChainedHashDictionary<String, Double>();
    	double additionalFreq = 1.0 / (double) words.size();
    	for (String word : words) {
    		if (!computedTfScores.containsKey(word)) {
    			computedTfScores.put(word, additionalFreq);
    		} else {
    			computedTfScores.put(word, computedTfScores.get(word) + additionalFreq);
    		}
    	}
    	return computedTfScores;
    }

    /**
     * Returns a dictionary mapping every webpage to its own TF-IDF vector.
     */
    private IDictionary<URI, IDictionary<String, Double>> computeAllDocumentTfIdfVectors(ISet<Webpage> pages) {
        IDictionary<URI, IDictionary<String, Double>> computedTfIdfVectors = 
        		new ChainedHashDictionary<URI, IDictionary<String, Double>>();
        for (Webpage webpage : pages) {
        	URI uri = webpage.getUri();
        	IList<String> words = webpage.getWords();
        	IDictionary<String, Double> tfIdfScores = new ArrayDictionary<String, Double>();
        	for (KVPair<String, Double> pair : this.computeTfScores(words)) {
        		String word = pair.getKey();
        		double tfIdf = pair.getValue() * idfScores.get(word);
        		tfIdfScores.put(word, tfIdf);
        	}
        	computedTfIdfVectors.put(uri, tfIdfScores);
        }
        return computedTfIdfVectors;
        
    }

    /**
     * Returns the cosine similarity between the TF-IDF vector for the given query and the
     * URI's document.
     *
     * Precondition: the given uri must have been one of the uris within the list of
     *               webpages given to the constructor.
     */
    public Double computeRelevance(IList<String> query, URI pageUri) {
    	if (!documentTfIdfVectors.containsKey(pageUri)) {
    		throw new IllegalArgumentException();
    	}
    	IDictionary<String, Double> documentVector = this.documentTfIdfVectors.get(pageUri);
    	IDictionary<String, Double> queryTermFrequency = this.computeTfScores(query);
    	
    	double numerator = 0.0;
    	double queryMagnitude = 0.0;
    	for (KVPair<String, Double> queryWord : queryTermFrequency) {
    		String word = queryWord.getKey();
    		double queryTfScore = queryWord.getValue();
    		double queryWordScore = 0.0;
    		if (this.idfScores.containsKey(word)) {
    			queryWordScore = queryTfScore * this.idfScores.get(word);
    		}
    		queryMagnitude += queryWordScore * queryWordScore;
    		double docWordScore = 0.0;
    		if (documentVector.containsKey(word)) {
    			docWordScore = documentVector.get(word);
    		}
    		numerator += docWordScore * queryWordScore;
    	}
    	queryMagnitude = Math.sqrt(queryMagnitude);
    	double denominator = this.documentTfIdfNormVectors.get(pageUri) * queryMagnitude;
    	if (denominator != 0.0) {
    		return numerator / denominator;
    	} else {
    		return 0.0;
    	}
    }
    
    /**
     * Returns a dictionary mapping each webpage to the magnitude of the TF-IDF vector.
     */
    private IDictionary<URI, Double> computeDocumentNormVector() {
    	IDictionary<URI, Double> documentNormVectors = new ArrayDictionary<URI, Double>();
    	for (KVPair<URI, IDictionary<String, Double>> pair : this.documentTfIdfVectors) {
    		URI uri = pair.getKey();
    		IDictionary<String, Double> vector = pair.getValue();
    		double norm = this.norm(vector);
    		documentNormVectors.put(uri, norm);
    	}
    	return documentNormVectors;
    }
    
    /**
     * Calculates the magnitude of a vector (the Euclidean norm magnitude).
     */
    private double norm(IDictionary<String, Double> vector) {
    	double output = 0.0;
    	for (KVPair<String, Double> pair : vector) {
    		double score = pair.getValue();
    		output += score * score;
    	}
    	return Math.sqrt(output);
    }
}
