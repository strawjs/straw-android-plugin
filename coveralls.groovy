#! /usr/bin/env groovy
// coveralls.groovy

import groovy.json.*

@Grab(group='org.codehaus.groovy.modules.http-builder', module='http-builder', version='0.5.0-RC2')

import groovyx.net.http.HTTPBuilder
import static groovyx.net.http.ContentType.URLENC

API_HOST = 'https://coveralls.io'
API_PATH = '/api/v1/jobs'

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
        JsonBuilder json = new JsonBuilder()
        json this
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
            def max = cov.max { it.key }

            List r = [null] * max.key
            cov.each { Integer line, Integer hits ->
                r[line] = hits
            }

            reports.add new SourceReport(filename, new File(sourceDir + filename).text, r)
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
    def http = new HTTPBuilder(API_HOST)

    http.post(path: API_PATH, body: [json_file: json], requestContentType: URLENC) { resp ->
        println resp.getClass()
    }
}

void main() {
    ServiceInfo serviceInfo = ServiceInfoFactory.createFromEnvVar()

    if (serviceInfo == null) {
        println 'no available service'

        return
    }

    File file = new File(COBERTURA_REPORT_PATH)

    if (!file.exists()) {
        println 'covertura report not available: ' + file.absolutePath

        return
    }

    List<SourceReport> sourceReports = SourceReportFactory.createFromCoberturaXML file

    Report rep = new Report(serviceInfo.serviceName, serviceInfo.serviceJobId, sourceReports)

    postToCoveralls(rep.toJson())
}

main()
