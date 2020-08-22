/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.flink.example.usercase.streaming.cep;

import javafx.beans.binding.StringBinding;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import org.flink.example.usercase.model.Event;
import org.flink.example.usercase.streaming.application.map.EventMapFunction;

import java.util.List;
import java.util.Map;

/**
 * Skeleton for a Flink Streaming Job.
 *
 * <p>For a tutorial how to write a Flink streaming application, check the
 * tutorials and examples on the <a href="http://flink.apache.org/docs/stable/">Flink Website</a>.
 *
 * <p>To package your application into a JAR file for execution, run
 * 'mvn clean package' on the command line.
 *
 * <p>If you change the name of the main class (with the public static void main(String[] args))
 * method, change the respective entry in the POM.xml file (simply search for 'mainClass').
 */
public class CEPStreamingJob {

	public static void main(String[] args) throws Exception {
		// the host and the port to connect to
		final String hostname;
		final int port;
		try {
			final ParameterTool params = ParameterTool.fromArgs(args);
			hostname = params.has("hostname") ? params.get("hostname") : "localhost";
			port = params.has("port") ? params.getInt("port") : 9999;
		} catch (Exception e) {
			System.err.println("No port specified. Please run 'SocketWindowWordCount " +
					"--hostname <hostname> --port <port>', where hostname (localhost by default) " +
					"and port is the address of the text server");
			System.err.println("To start a simple text server, run 'netcat -l <port>' and " +
					"type the input text into the command line");
			return;
		}
		Pattern<Event, ?> parttern = Pattern.<Event>begin("start")
				.where(new SimpleCondition<Event>() {
					public boolean filter(Event event) {
						System.out.println(event.getKey());
						return event.getKey().equals("1");
					}
		}).next("middle").where(new SimpleCondition<Event>() {
					@Override
					public boolean filter(Event event) throws Exception {
						System.out.println(event.getKey());
						return event.getKey().equals("3");
					}
				}).followedBy("end").where(new SimpleCondition<Event>() {
					@Override
					public boolean filter(Event event) throws Exception {
						System.out.println(event.getKey());
						return event.getKey().equals("4");
					}
				});

		// get the execution environment
		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

		// get input data by connecting to the socket
		DataStream<String> text = env.socketTextStream(hostname, port, "\n");
		DataStream<Event> inputEvent = text.map(new EventMapFunction());
		PatternStream<Event> patternStream = CEP.pattern(inputEvent, parttern);
		patternStream.select(new PatternSelectFunction<Event, Object>() {

			@Override
			public Object select(Map<String, List<Event>> map) throws Exception {
				StringBuilder builder = new StringBuilder();
				for (String eventKey: map.keySet()) {
					builder.append("output:").append(eventKey).append(" ").append(map.get(eventKey).toString());
				}
				return builder.toString();
			}
		}).print();
		env.execute("Socket CEP");
	}


}
