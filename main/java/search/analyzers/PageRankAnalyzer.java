/*
 * Name: Chris Forbes
 * Date: 11/29/2017
 * File: PageRankAnalyzer.java
 */
package search.analyzers;

import datastructures.concrete.ChainedHashSet;
import datastructures.concrete.KVPair;
import datastructures.concrete.dictionaries.ArrayDictionary;
import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.ISet;
import search.models.Webpage;

import java.net.URI;

/**
 * This class is responsible for computing the 'page rank' of all available webpages.
 * If a webpage has many different links to it, it should have a higher page rank.
 * See the spec for more details.
 */
public class PageRankAnalyzer {
    private IDictionary<URI, Double> pageRanks;

    /**
     * Computes a graph representing the internet and computes the page rank of all
     * available webpages.
     *
     * @param webpages  A set of all webpages we have parsed.
     * @param decay     Represents the "decay" factor when computing page rank (see spec).
     * @param epsilon   When the difference in page ranks is less then or equal to this number,
     *                  stop iterating.
     * @param limit     The maximum number of iterations we spend computing page rank. This value
     *                  is meant as a safety valve to prevent us from infinite looping in case our
     *                  page rank never converges.
     */
    public PageRankAnalyzer(ISet<Webpage> webpages, double decay, double epsilon, int limit) {
        // Step 1: Make a graph representing the 'internet'
        IDictionary<URI, ISet<URI>> graph = this.makeGraph(webpages);

        // Step 2: Use this graph to compute the page rank for each webpage
        this.pageRanks = this.makePageRanks(graph, decay, limit, epsilon);
    }

    /**
     * This method converts a set of webpages into an unweighted, directed graph,
     * in adjacency list form.
     *
     * You may assume that each webpage can be uniquely identified by its URI.
     *
     * Note that a webpage may contain links to other webpages that are *not*
     * included within set of webpages you were given. You should omit these
     * links from your graph: we want the final graph we build to be
     * entirely "self-contained".
     */
    private IDictionary<URI, ISet<URI>> makeGraph(ISet<Webpage> webpages) {
        IDictionary<URI, ISet<URI>> graph = new ArrayDictionary<URI, ISet<URI>>();
        ISet<URI> webpageUris = new ChainedHashSet<URI>();
        for (Webpage webpage : webpages) {
        	webpageUris.add(webpage.getUri());
        }
        for (Webpage webpage : webpages) {
        	URI pageUri = webpage.getUri();
        	ISet<URI> links = new ChainedHashSet<URI>();
        	for (URI uri : webpage.getLinks()) {
        		if (!uri.equals(pageUri) && !links.contains(uri) && webpageUris.contains(uri)) {
        			links.add(uri);
        		}
        	}
        	graph.put(pageUri, links);
        }
        return graph;
    }

    /**
     * Computes the page ranks for all webpages in the graph.
     *
     * Precondition: assumes 'this.graphs' has previously been initialized.
     *
     * @param decay     Represents the "decay" factor when computing page rank (see spec).
     * @param epsilon   When the difference in page ranks is less then or equal to this number,
     *                  stop iterating.
     * @param limit     The maximum number of iterations we spend computing page rank. This value
     *                  is meant as a safety valve to prevent us from infinite looping in case our
     *                  page rank never converges.
     */
    private IDictionary<URI, Double> makePageRanks(IDictionary<URI, ISet<URI>> graph,
                                                   double decay,
                                                   int limit,
                                                   double epsilon) {
    	IDictionary<URI, Double> computedPageRanks = new ChainedHashDictionary<URI, Double>();
    	IDictionary<URI, Double> oldPageRanks = new ChainedHashDictionary<URI, Double>();
    	double initPageRank = 1.0 / (double) graph.size();
    	double newSurfers = (1.0 - decay) / (double) graph.size();
    	for (KVPair<URI, ISet<URI>> pair : graph) {
    		URI pageUri = pair.getKey();
    		oldPageRanks.put(pageUri, initPageRank);
    	}

    	if (limit == 0) {
    		return oldPageRanks;
    	}
        for (int i = 0; i < limit; i++) {
        	for (KVPair<URI, ISet<URI>> webpage : graph) {
        		URI uri = webpage.getKey();
        		computedPageRanks.put(uri, newSurfers);
        	}
        	for (KVPair<URI, ISet<URI>> webpage : graph) {
        		URI pageUri = webpage.getKey();
        		ISet<URI> links = webpage.getValue();
        		double oldPageRank = oldPageRanks.get(pageUri);
        		if (links.isEmpty()) {
        			// d * oldPR / N
        			for (KVPair<URI, ISet<URI>> page : graph) {
        				URI uri = page.getKey();
        				double currPageRank = computedPageRanks.get(uri);
        				double update = ((decay * oldPageRank) / ((double) graph.size()));
        				computedPageRanks.put(uri, currPageRank + update);
        			}
        		} else {
        			// d * oldPR / outgoing links
        			for (URI uri : links) {
        				double update = ((decay * oldPageRank) / ((double) links.size()));
        				double currPageRank = computedPageRanks.get(uri);
        				computedPageRanks.put(uri, currPageRank + update);
        			}
        		}
        	}

        	boolean converged = true;
        	for (KVPair<URI, Double> pair : computedPageRanks) {
        		URI pageUri = pair.getKey();
        		double currPageRank = pair.getValue();
        		double oldPageRank = oldPageRanks.get(pageUri);
        		double difference = Math.abs(currPageRank - oldPageRank);
        		if (converged) { 
        			converged = (difference <= epsilon);
        		}
        		oldPageRanks.put(pageUri, currPageRank);
        	}
        	if (converged) {
        		return computedPageRanks;
        	}
        }
        return computedPageRanks;
    }

    /**
     * Returns the page rank of the given URI.
     *
     * Precondition: the given uri must have been one of the uris within the list of
     *               webpages given to the constructor.
     */
    public double computePageRank(URI pageUri) {
    	return this.pageRanks.containsKey(pageUri) ? this.pageRanks.get(pageUri) : 0.0;
    }
}
