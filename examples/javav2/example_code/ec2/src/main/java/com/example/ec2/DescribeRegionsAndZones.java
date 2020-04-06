//snippet-sourcedescription:[DescribeRegionsAndZones.java demonstrates how to get information about all the regions and zones from an EC2 client.]
//snippet-keyword:[SDK for Java 2.0]
//snippet-keyword:[Code Sample]
//snippet-service:[ec2]
//snippet-sourcetype:[full-example]
//snippet-sourcedate:[11/02/2020]
//snippet-sourceauthor:[soo-aws]
/*
 * Copyright 2010-2020 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package com.example.ec2;
// snippet-start:[ec2.java2.describe_region_and_zones.complete]
// snippet-start:[ec2.java2.describe_region_and_zones.import]
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.DescribeRegionsResponse;
import software.amazon.awssdk.services.ec2.model.Region;
import software.amazon.awssdk.services.ec2.model.AvailabilityZone;
import software.amazon.awssdk.services.ec2.model.Ec2Exception;
import software.amazon.awssdk.services.ec2.model.DescribeAvailabilityZonesResponse;
// snippet-end:[ec2.java2.describe_region_and_zones.import]

/**
 * Describes all regions and zones
 */
public class DescribeRegionsAndZones {

    public static void main(String[] args) {
        // snippet-start:[ec2.java2.describe_region_and_zones.main]
        // snippet-start:[ec2.java2.describe_region_and_zones.client]
        Ec2Client ec2 = Ec2Client.create();
        // snippet-end:[ec2.java2.describe_region_and_zones.client]

        // snippet-start:[ec2.java2.describe_region_and_zones.region]
        try {

            DescribeRegionsResponse regionsResponse = ec2.describeRegions();

            for(Region region : regionsResponse.regions()) {
                System.out.printf(
                    "Found region %s " +
                            "with endpoint %s",
                    region.regionName(),
                    region.endpoint());
                System.out.println();
            // snippet-end:[ec2.java2.describe_region_and_zones.region]
        }

            // snippet-start:[ec2.java2.describe_region_and_zones.avail_zone]
            DescribeAvailabilityZonesResponse zonesResponse =
                ec2.describeAvailabilityZones();

            for(AvailabilityZone zone : zonesResponse.availabilityZones()) {
                System.out.printf(
                    "Found availability zone %s " +
                            "with status %s " +
                            "in region %s",
                    zone.zoneName(),
                    zone.state(),
                    zone.regionName());
                System.out.println();
            // snippet-end:[ec2.java2.describe_region_and_zones.avail_zone]
        }

    } catch (Ec2Exception e) {
        e.getStackTrace();
    }
        // snippet-end:[ec2.java2.describe_region_and_zones.main]
    }
}

// snippet-end:[ec2.java2.describe_region_and_zones.complete]
