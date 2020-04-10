package com.admin.lucene;//package com.ofunx.lucene;
//
//import com.bdqn.blog.entity.Blog;
//import com.bdqn.blog.utils.DateUtil;
//import com.bdqn.blog.utils.SystemConstants;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.lucene.analysis.TokenStream;
//import org.apache.lucene.document.Document;
//import org.apache.lucene.document.Field;
//import org.apache.lucene.document.StringField;
//import org.apache.lucene.document.TextField;
//import org.apache.lucene.index.*;
//import org.apache.lucene.queryparser.classic.QueryParser;
//import org.apache.lucene.search.*;
//import org.apache.lucene.search.highlight.*;
//import org.apache.lucene.store.Directory;
//import org.apache.lucene.store.FSDirectory;
//import org.springframework.stereotype.Component;
//import org.wltea.analyzer.lucene.IKAnalyzer;
//
//import java.io.IOException;
//import java.io.StringReader;
//import java.nio.file.Paths;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.concurrent.locks.ReentrantLock;
//
//@Component
//public class BlogIndex {
//    private Directory directory;
//
//    public IndexWriter getInexWriter() {
//
//        try {
//            directory = FSDirectory.open(Paths.get(SystemConstants.LUCENE_REPOSITORY));
//            IndexWriterConfig config = new IndexWriterConfig(new IKAnalyzer());
//            IndexWriter indexWriter = new IndexWriter(directory, config);
//            return indexWriter;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public void addBlog(Blog blog) {
//        ReentrantLock lock = new ReentrantLock();
//        lock.lock();
//        IndexWriter indexWriter = getInexWriter();
//        try {
//
//            Document document = new Document();
//            document.add(new StringField("id", blog.getId().toString(), Field.Store.YES));
//            document.add(new StringField("releaseDate", DateUtil.formatDate(new Date(), "yyyy-MM-dd").toString(), Field.Store.YES));
//            document.add(new StringField("createDate", DateUtil.formatDate(new Date(), "yyyy-MM-dd").toString(), Field.Store.YES));
//            document.add(new TextField("title", blog.getTitle(), Field.Store.YES));
//            document.add(new TextField("content", blog.getSummary(), Field.Store.YES));
//            document.add(new TextField("userName", blog.getUserName(), Field.Store.YES));
//            indexWriter.addDocument(document);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                indexWriter.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        lock.unlock();
//    }
//
//    public void updateBlog(Blog blog) {
//        ReentrantLock lock = new ReentrantLock();
//        lock.lock();
//        IndexWriter indexWriter = getInexWriter();
//        try {
//
//            Document document = new Document();
//            document.add(new StringField("id", blog.getId().toString(), Field.Store.YES));
//            document.add(new StringField("releaseDate", DateUtil.formatDate(new Date(), "yyyy-MM-dd").toString(), Field.Store.YES));
//            document.add(new StringField("createDate", DateUtil.formatDate(new Date(), "yyyy-MM-dd").toString(), Field.Store.YES));
//            document.add(new TextField("title", blog.getTitle(), Field.Store.YES));
//            document.add(new TextField("content", blog.getSummary(), Field.Store.YES));
//            indexWriter.updateDocument(new Term("id", blog.getId().toString()), document);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                indexWriter.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        lock.unlock();
//    }
//
//    public void deleteIndex(String blogId) {
//        ReentrantLock lock = new ReentrantLock();
//        lock.lock();
//
//        try {
//            IndexWriter indexWriter = getInexWriter();
//            indexWriter.deleteDocuments(new Term("id", blogId));
//            indexWriter.forceMergeDeletes();
//            indexWriter.commit();
//            indexWriter.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            lock.unlock();
//        }
//
//    }
//
//    public List<Blog> searchBlog(String keyWord) {
//        List<Blog> blogList = new ArrayList<>();
//        try {
//            directory = FSDirectory.open(Paths.get(SystemConstants.LUCENE_REPOSITORY));
//            IndexReader indexReader = DirectoryReader.open(directory);
//            IndexSearcher indexSearcher = new IndexSearcher(indexReader);
//            IKAnalyzer analyzer = new IKAnalyzer();
//            QueryParser parserTitle = new QueryParser("title",analyzer);
//            Query queryTitle = parserTitle.parse(keyWord);
//            QueryParser parserContent = new QueryParser("content",analyzer);
//            Query queryContent = parserContent.parse(keyWord);
//
//            BooleanQuery.Builder builder = new BooleanQuery.Builder();
//            builder.add(queryTitle, BooleanClause.Occur.SHOULD);
//            builder.add(queryContent, BooleanClause.Occur.SHOULD);
//            TopDocs topDocs = indexSearcher.search(builder.build(),100);
//            QueryScorer queryScorer = new QueryScorer(queryTitle);
//            Fragmenter fragmenter = new SimpleSpanFragmenter(queryScorer);
//            SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<b><font color = 'red'>","</font></b>");
//            Highlighter highlighter = new Highlighter(simpleHTMLFormatter,queryScorer);
//            highlighter.setTextFragmenter(fragmenter);
//            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
//                Document doc = indexSearcher.doc(scoreDoc.doc);
//                Blog blog = new Blog();
//                blog.setId(Long.getLong(doc.get("id")));
//                blog.setCreateDateStr(doc.get("createDate"));
//                blog.setUserName(doc.get("userName"));
//                String title = doc.get("title");
//                String content = doc.get("content");
//                if (StringUtils.isNotEmpty(title)){
//                    TokenStream tokenStream = analyzer.tokenStream("title",new StringReader(title));
//                    String hTitle = highlighter.getBestFragment(tokenStream,title);
//                    if (StringUtils.isEmpty(hTitle))
//                        blog.setTitle(title);
//                    else
//                        blog.setTitle(hTitle);
//                }
//                if (StringUtils.isNotEmpty(content)){
//                    TokenStream tokenStream = analyzer.tokenStream("content",new StringReader(content));
//                    String hContent = highlighter.getBestFragment(tokenStream,content);
//                    if (StringUtils.isEmpty(hContent))
//                        if (content.length()<=200)
//                          blog.setContent(content);
//                        else
//                            blog.setContent(content.substring(0,200));
//                    else
//                        blog.setContent(content);
//                }
//                blogList.add(blog);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return blogList;
//    }
//}
