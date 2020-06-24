/*
 * Copyright 2017-2020 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.views.rocker;

import com.fizzed.rocker.BindableRockerModel;
import io.micronaut.core.io.Writable;
import io.micronaut.core.util.ArgumentUtils;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Produces;
import io.micronaut.views.ViewsConfiguration;
import io.micronaut.views.ViewsRenderer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;

/**
 * Renders templates with Rocker.
 *
 * @author Sam Adams
 * @since 1.3.2
 */
@Produces(MediaType.TEXT_HTML)
@Singleton
public class RockerViewsRenderer implements ViewsRenderer {

    protected final RockerEngine rockerEngine;
    protected final ViewsConfiguration viewsConfiguration;
    protected final RockerViewsRendererConfiguration rockerConfiguration;
    protected final String folder;

    /**
     * @param viewsConfiguration  Views Configuration
     * @param rockerConfiguration Rocker Configuration
     * @param rockerEngine        Rocker Engine
     */
    @Inject
    RockerViewsRenderer(ViewsConfiguration viewsConfiguration,
                        RockerViewsRendererConfiguration rockerConfiguration,
                        RockerEngine rockerEngine) {
        this.viewsConfiguration = viewsConfiguration;
        this.rockerConfiguration = rockerConfiguration;
        this.rockerEngine = rockerEngine;
        this.folder = viewsConfiguration.getFolder();
    }

    @Override
    @Nonnull public Writable render(@Nonnull String view, @Nullable Object data) {
        ArgumentUtils.requireNonNull("view", view);

        Map<String, Object> context = modelOf(data);
        BindableRockerModel model = rockerConfiguration.isRelaxed()
                ? rockerEngine.template(view).relaxedBind(context)
                : rockerEngine.template(view).bind(context);
        return new RockerWritable(model);
    }

    @Override
    public boolean exists(@Nonnull String viewName) {
        return rockerEngine.exists(viewName);
    }
    
}
