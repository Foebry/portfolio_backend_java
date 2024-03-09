package routes;

import java.io.IOException;
import java.io.OutputStream;

import annotations.Route;
import entities.Response;
import services.Request;

public class StudyRoute extends BaseRoute {
    // Controller<Study> controller;

    public StudyRoute(Request request) {
        super(request);
        // this.controller = new Controller<Study>();
    }

    // @Route(endpoint = "/studies", method = "GET", name = "listStudies")
    // public OutputStream listStudies() {
    // return this.getResponse().ok("OK");
    // }

    // @Route(endpoint = "/studies/{id}", method = "GET", name = "getStudyById")
    // private OutputStream getStudyById(int id) {
    // Study study = this.controller.getById(id);
    // if (study == null)
    // return this.getResponse().notFound();

    // return study.serialize();
    // }

    // @Route(endpoint = "studies/{id}", method = "DELETE", name =
    // "deleteStudyById")
    // private OutputStream deleteStudyById(int id) {
    // Study study = this.controller.getById(id);
    // if (study == null)
    // return this.getResponse().notFound();

    // this.controller.delete(study);

    // return this.getResponse().noContent();
    // }

    // @Route(endpoint = "studies/{id}", method = "PUT", name = "updateStudy")
    // private OutputStream updateStudyById(int id) {
    // return this.getResponse().ok("OK");
    // }

    // @Route(endpoint = "studies/{id}", method = "PATCH", name = "patchStudy")
    // private OutputStream patchStudyById(int id) {
    // return this.getResponse().ok("OK");
    // }
}
