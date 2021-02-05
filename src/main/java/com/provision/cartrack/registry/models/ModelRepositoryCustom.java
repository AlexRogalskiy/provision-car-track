package com.provision.cartrack.registry.models;

import com.provision.cartrack.helpers.Pagination;

public interface ModelRepositoryCustom {

	Pagination<Model> search(Pagination<Model> request);

}
