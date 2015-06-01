/*
 * Copyright 2015 Kaazing Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kaazing.staf_aeron;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;


public class YAMLParser {


    static STAFHosts parseHosts(String filename)
    {
        Constructor constructor = new Constructor(STAFHosts.class);
        Yaml file = new Yaml(constructor);
        InputStream input = null;

        try
        {
            input = new FileInputStream(new File(filename));
        }
        catch (Exception e)
        {

        }
        STAFHosts hosts = (STAFHosts) file.load(input);
        return hosts;
    }
}
