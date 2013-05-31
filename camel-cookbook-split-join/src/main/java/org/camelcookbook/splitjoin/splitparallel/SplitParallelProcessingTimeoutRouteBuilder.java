/*
 * Copyright (C) Scott Cranton and Jakub Korab
 * https://github.com/CamelCookbook
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.camelcookbook.splitjoin.splitparallel;

import org.apache.camel.builder.RouteBuilder;

class SplitParallelProcessingTimeoutRouteBuilder extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("direct:in")
            .split(body()).parallelProcessing().timeout(1000)
                .log("Processing message[${property.CamelSplitIndex}]")
                .to("direct:delay20th")
            .end()
            .to("mock:out");

        from("direct:delay20th")
            .choice()
                .when(simple("${property.CamelSplitIndex} == 20"))
                    .to("direct:longDelay")
                .otherwise()
                    .to("mock:split")
            .endChoice();

        from("direct:longDelay")
            .delay(1500)
            .to("mock:delayed");
    }
}
