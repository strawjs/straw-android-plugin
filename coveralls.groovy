#! /usr/bin/env groovy
// coveralls.groovy

import groovy.json.*

@Grab(group='org.codehaus.groovy.modules.http-builder', module='http-builder', version='0.5.0-RC2')

import groovyx.net.http.HTTPBuilder

import static groovyx.net.http.ContentType.URLENC
import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.ContentType.TEXT
import static groovyx.net.http.Method.POST

@Grab(group='org.apache.httpcomponents', module='httpmime', version='4.3') 

import org.apache.http.entity.mime.MultipartEntityBuilder
import org.apache.http.entity.mime.HttpMultipartMode
import org.apache.http.entity.mime.content.StringBody
import org.apache.http.entity.mime.content.FileBody

@Grab(group='org.apache.httpcomponents', module='httpcore', version='4.3') 

import org.apache.http.HttpEntity
import org.apache.http.HttpResponse
import org.apache.http.entity.ContentType

@Grab(group='org.apache.httpcomponents', module='httpclient', version='4.3') 

import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpPost

API_HOST = 'https://coveralls.io'
API_PATH = '/api/v1/jobs'
//API_HOST = 'http://localhost'
//API_PATH = '/test.php'

COBERTURA_REPORT_PATH = 'build/reports/cobertura/coverage.xml'

// model for coveralls io report's source file report
class SourceReport {
    String name;
    String source;
    List<Integer> coverage;

    public SourceReport(String name, String source, List<Integer> coverage) {
        this.name = name;
        this.source = source;
        this.coverage = coverage;
    }

}

// model for coveralls io report
class Report {
    String service_job_id;
    String service_name;
    List<SourceReport> source_files;

    public Report() {
    }

    public Report(String serviceName, String serviceJobId, List<SourceReport> sourceFiles) {
        this.service_name = serviceName;
        this.service_job_id = serviceJobId;
        this.source_files = sourceFiles;
    }

    public String toJson() {
        JsonBuilder json = new JsonBuilder(this)
        return json.toString()
    }
}

class SourceReportFactory {

    public static List<SourceReport> createFromCoberturaXML(File file) {
        Node coverage = new XmlParser().parse(file)
        String sourceDir = coverage.sources.source.text() + '/'

        Map a = [:]

        coverage.packages.package.classes.class.each() {
            Map cov = a.get(it.'@filename', [:])

            it.lines.line.each() {
                cov[it.'@number'.toInteger()] = it.'@hits'.toInteger()
            }
        }

        List<SourceReport> reports = new ArrayList<SourceReport>()

        a.each { String filename, Map cov ->
            String source = new File(sourceDir + filename).text

            List r = [null] * source.readLines().size()
            cov.each { Integer line, Integer hits ->
                r[line] = hits
            }

            reports.add new SourceReport(filename, source, r)
        }

        return reports

    }
}

// model for ci service info
class ServiceInfo {
    String serviceName;
    String serviceJobId;

    public ServiceInfo(String serviceName, String serviceJobId) {
        this.serviceName = serviceName;
        this.serviceJobId = serviceJobId;
    }
}

class ServiceInfoFactory {

    public static createFromEnvVar() {

        if (System.getenv('TRAVIS') == 'true') {
            return new ServiceInfo('travis-ci', System.getenv('TRAVIS_JOB_ID'))
        }

        // cannot create service info from env var
        return null

    }

}

void postToCoveralls(String json) {

    //*
    new File('send.txt').text = json

    HttpClient c = new DefaultHttpClient()
    HttpPost p = new HttpPost(API_HOST + API_PATH)

    MultipartEntityBuilder builder = MultipartEntityBuilder.create()
    builder.addPart('json_file', new FileBody(new File('send.txt'), ContentType.APPLICATION_JSON, 'json_file'))
    //builder.setMode(HttpMultipartMode.RFC6532)
    //builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
    //builder.setMode(HttpMultipartMode.STRICT)
    p.setEntity(builder.build())

    HttpResponse resp = c.execute(p)
    HttpEntity e = resp.getEntity()

    System.out << e.getContent()
    //*/

    /*
    HTTPBuilder http = new HTTPBuilder(API_HOST)

    http.request(POST, TEXT) { req ->
        uri.path = API_PATH
        requestContentType = 'multipart/form-data'

        MultipartEntityBuilder builder = MultipartEntityBuilder.create()
        builder.addPart('json_file', new StringBody(json, ContentType.APPLICATION_JSON))
        req.entity = builder.build()

        response.success = { resp, reader ->
            println resp.statusLine
            println resp.getAllHeaders()
            println resp.getData()
            System.out << reader
        }

        response.failure = { resp ->
            println resp.statusLine
            println resp.getAllHeaders()
            println resp.getData()
        }
    }
    //*/
}

void main() {
    ServiceInfo serviceInfo = ServiceInfoFactory.createFromEnvVar()

    if (serviceInfo == null) {
        println 'no available service'

        return
    }

    println 'service name: ' + serviceInfo.serviceName
    println 'service job id: ' + serviceInfo.serviceJobId

    File file = new File(COBERTURA_REPORT_PATH)

    if (!file.exists()) {
        println 'covertura report not available: ' + file.absolutePath

        return
    }

    println 'cobertura report file: ' + file.absolutePath

    List<SourceReport> sourceReports = SourceReportFactory.createFromCoberturaXML file

    Report rep = new Report(serviceInfo.serviceName, serviceInfo.serviceJobId, sourceReports)

    println rep.toJson()

    postToCoveralls rep.toJson()
}

main()
